package com.simplespring;

import com.simplespring.ioc.xml.XmlBeanFactory;
import org.junit.Test;

public class test {
    public static void main(String[] args) {
        Class rc = null;
        try {
            rc = Class.forName("com.simplespring.HelloServiceImp1");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(rc);
    }
}
