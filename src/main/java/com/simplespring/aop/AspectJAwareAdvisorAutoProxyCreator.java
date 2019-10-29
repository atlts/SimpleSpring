package com.simplespring.aop;

import com.simplespring.ioc.BeanPostProcessor;
import com.simplespring.ioc.factory.BeanFactory;
import com.simplespring.ioc.factory.BeanFactoryAware;
import com.simplespring.ioc.xml.XmlBeanFactory;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.List;

/**
 * 将目标bean转化为相应的代理
 */
public class AspectJAwareAdvisorAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {
    private XmlBeanFactory xmlBeanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        /*
        *对切面和方法拦截器显然不需要进行代理
         */
        if(bean instanceof AspectJExpressionPointcutAdvisor){
           return bean;
       }
        if(bean instanceof MethodInterceptor){
            return bean;
        }
        /**
         * 1.  从 BeanFactory 查找 AspectJExpressionPointcutAdvisor 类型的对象
         * 也就是xml文件中注册的切面的对象，对象中有方法拦截器（Advice）和expression（Pointcut）两个属性
         * expression自动注入了pointcut当中，以此判断相应的类和方法是否是切点
          */

        List<AspectJExpressionPointcutAdvisor>advisors =
                xmlBeanFactory.getBeanForType(AspectJExpressionPointcutAdvisor.class);
        for(AspectJExpressionPointcutAdvisor advisor : advisors){
            // 2. 使用 Pointcut 对象匹配当前 bean 对象
            if(advisor.getPoint().getClassFilter().matchers(bean.getClass())){
                /**
                 * 匹配之后就要生成代理
                 */
                ProxyFactory advisedSupport = new ProxyFactory();
                TargetSource targetSource = new TargetSource(bean,bean.getClass(),bean.getClass().getInterfaces());
                advisedSupport.setTargetSource(targetSource);
                advisedSupport.setMethodInterceptor((MethodInterceptor)advisor.getAdvice());
                advisedSupport.setMethodMatcher(advisor.getPoint().getMethodMatcher());
                return advisedSupport.getProxy();
            }
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws Exception {
        xmlBeanFactory = (XmlBeanFactory)beanFactory;
    }
}
