package com.simplespring.ioc;

/**
 * 解析xml文件的接口
 */
public interface BeanDefinitionReader {
    void loadBeanDefinitions(String location) throws Exception;
}
