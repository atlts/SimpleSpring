package com.simplespring.ioc.xml;

import com.simplespring.Car;
import com.simplespring.HelloService;
import com.simplespring.Wheel;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlBeanFactoryTest {
    @Test
    public void getBean()throws Exception{
        String location = XmlBeanFactory.class.getClassLoader().getResource("spring-test.xml").getFile();
       // System.out.println(Class.forName("com.simplespring.HelloServiceImp1"));
        XmlBeanFactory beanFactory = new XmlBeanFactory(location);
        Wheel wheel = (Wheel)beanFactory.getBean("wheel");
        Car car = (Car)beanFactory.getBean("car");
        System.out.println(wheel);
        System.out.println(car);

        HelloService helloService = (HelloService)beanFactory.getBean("helloService");
        helloService.sayHelloWorld();
    }
}