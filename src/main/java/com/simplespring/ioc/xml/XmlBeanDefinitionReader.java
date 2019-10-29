package com.simplespring.ioc.xml;

import com.simplespring.ioc.BeanDefinition;
import com.simplespring.ioc.BeanReference;
import com.simplespring.ioc.PropertyValue;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlBeanDefinitionReader {
    /**
     * 这个作为BeanDefinition的容器，把属性填充进list里面就完事了
     */
    private Map<String, BeanDefinition>registry;
    public XmlBeanDefinitionReader(){
        registry = new HashMap<String, BeanDefinition>();
    }

    /**
     * 读取xml文件
     * @param location
     * @throws Exception
     */
    public void loadBeanDefinition(String location)throws Exception{
        InputStream inputStream = new FileInputStream(location);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(inputStream);
        Element root = doc.getDocumentElement();
        parseBeanDefinitions(root);
    }

    /**
     *
     * @param root
     * @throws Exception
     * 将xml文件中一个一个bean填充好
     */
    private void parseBeanDefinitions(Element root)throws Exception{
        NodeList nodes = root.getChildNodes();
        for(int i = 0;i < nodes.getLength();i++){
            Node node = nodes.item(i);//这个应该是获得bean那一行的属性了
            if(node instanceof Element){
                Element ele = (Element)node;
                parseBeanDefinition(ele);
            }
        }
    }

    /**
     *
     * @param ele
     * @throws Exception
     * 生成一个beanDefinition对象填充完成之后，放入registry容器中
     *此时还没有注入bean属性
     */

    private void parseBeanDefinition(Element ele)throws Exception{
        String name = ele.getAttribute("id");
        String className = ele.getAttribute("class");
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName(className);
      //  System.out.println(Class.forName(className));
        beanDefinition.setBeanClass(Class.forName(className));
        processProperty(ele,beanDefinition);
        registry.put(name,beanDefinition);
    }

    /**
     * @param ele
     * @param beanDefinition
     * 将ele中引用或属性填充进beanDefinition
     * 此处填充进去的是一个个propertyValue，并不是生成了实际的bean对象，以此加快初始化速度
     */
    private void processProperty(Element ele,BeanDefinition beanDefinition){
        NodeList propertyNodes = ele.getElementsByTagName("property");
        for(int i = 0;i < propertyNodes.getLength();i++){
            Node propertyNode = propertyNodes.item(i);
            if(propertyNode instanceof Element){
                Element propertyElement = (Element)propertyNode;
                String name = propertyElement.getAttribute("name");
                String value = propertyElement.getAttribute("value");
                if(value != null && value.length() > 0){
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name,value));
                }else{
                    String ref = propertyElement.getAttribute("ref");
                    if(ref == null || ref.length() == 0){
                        throw new IllegalArgumentException("value or ref config error");//这里稍微改进了一下
                    }
                    BeanReference beanReference = new BeanReference(ref);
                    beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name,beanReference));
                }
            }
        }
    }
    public Map<String,BeanDefinition>getRegistry(){
        return registry;
    }
}
