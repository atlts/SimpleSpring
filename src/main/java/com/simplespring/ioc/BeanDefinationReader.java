package com.simplespring.ioc;

/**
 * 解析xml文件的接口
 */
public interface BeanDefinationReader {
    void loadBeanDefinations(String location) throws Exception;
}
