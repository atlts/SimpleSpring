package com.simplespring.ioc.xml;

import com.simplespring.ioc.BeanDefinition;
import com.simplespring.ioc.BeanPostProcessor;
import com.simplespring.ioc.BeanReference;
import com.simplespring.ioc.PropertyValue;
import com.simplespring.ioc.factory.BeanFactory;
import com.simplespring.ioc.factory.BeanFactoryAware;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlBeanFactory implements BeanFactory {
    private Map<String, BeanDefinition>beanDefinitionMap = new HashMap<String, BeanDefinition>();
    private List<String> beanDefinitionNames = new ArrayList<String>();
    private List<BeanPostProcessor>beanPostProcessors = new ArrayList<BeanPostProcessor>();
    private XmlBeanDefinitionReader beanDefinitionReader;

    public XmlBeanFactory(String location)throws Exception{
        beanDefinitionReader = new XmlBeanDefinitionReader();
        loadBeanDefinitions(location);
    }

    /**
     * 通过beanDefinition获得bean的过程，这里就包括了利用反射机制延迟初始化
     * @param name
     * @return
     * @throws Exception
     */
    public Object getBean(String name) throws Exception{
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if(beanDefinition == null){
            throw new Exception("there is no bean with this name: " + name);
        }
        Object bean = beanDefinition.getBean();
        //这里就是延迟初始化
        if(bean == null){
            bean = createBean(beanDefinition);
            bean = initializeBean(bean,name);
            beanDefinition.setBean(bean);
        }
        return bean;
    }

    /**
     * 利用反射机制
     * @param bd
     * @return
     * @throws Exception
     */
    private Object createBean(BeanDefinition bd)throws Exception{
        Object bean = bd.getBeanClass().newInstance();
        applyPropertyValues(bean,bd);
        return bean;
    }
    //将配置文件中配置的属性填充到刚刚创建的 bean 对象中
    private void applyPropertyValues(Object bean,BeanDefinition bd) throws Exception{
        //检查 bean 对象是否实现了 Aware 一类的接口，如果实现了则把相应的依赖设置到 bean 对象中。
        // simple-spring 目前仅对 BeanFactoryAware 接口实现类提供了支持
        if(bean instanceof BeanFactoryAware){
            ((BeanFactoryAware)bean).setBeanFactory(this);
        }
        for(PropertyValue propertyValue : bd.getPropertyValues().getPropertyValues()){
            Object value = propertyValue.getValue();
            /**
             * 此处需要注意，若bean中调用的引用没有提前注入beanDefinition的容器中，会抛出异常
             */
            if(value instanceof BeanReference){
                BeanReference beanReference = (BeanReference)value;
                value = getBean(beanReference.getName());
            }try{
                Method declareMethod = bean.getClass().getDeclaredMethod(
                        "set" + propertyValue.getName().substring(0,1).toUpperCase() +
                                propertyValue.getName().substring(1),value.getClass()
                );
                declareMethod.setAccessible(true);
                declareMethod.invoke(bean,value);
            }catch(NoSuchMethodException e){
                Field declareField = bean.getClass().getDeclaredField(propertyValue.getName());
                declareField.setAccessible(true);
                declareField.set(bean,value);
            }
        }
    }
    //与AOP联系起来
    private Object initializeBean(Object bean,String name) throws Exception{
        for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
            bean = beanPostProcessor.postProcessBeforeInitialization(bean,name);
        }
        for(BeanPostProcessor beanPostProcessor : beanPostProcessors){
            bean = beanPostProcessor.postProcessAfterInitialization(bean,name);
        }
        return bean;
    }

    private void loadBeanDefinitions(String location) throws Exception{
        beanDefinitionReader.loadBeanDefination(location);
        registerBeanDefinition();
        registerBeanPostProcessor();
    }
    /**
     * 将封装好的 BeanDefinition 对象注册到 BeanDefinition 容器中
     */
    private void registerBeanDefinition(){
        for(Map.Entry<String,BeanDefinition> entry : beanDefinitionReader.getRegistry().entrySet()){
            String name = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            beanDefinitionMap.put(name,beanDefinition);
            beanDefinitionNames.add(name);
        }
    }

    public void registerBeanPostProcessor() throws Exception{
        List beans = getBeanForType(BeanPostProcessor.class);
        for(Object bean :beans){
            addBeanPostProcessor((BeanPostProcessor)bean);
        }
    }

    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
        beanPostProcessors.add(beanPostProcessor);
    }
    public List getBeanForType(Class type) throws Exception{
        List beans = new ArrayList();
        //根据 BeanDefinition 记录的信息，寻找所有实现了 BeanPostProcessor 接口的类。
        for(String beanDefinitionName : beanDefinitionNames){
            if(type.isAssignableFrom(beanDefinitionMap.get(beanDefinitionName).getBeanClass())){
                beans.add(getBean(beanDefinitionName));
            }
        }
        return beans;
    }
}
