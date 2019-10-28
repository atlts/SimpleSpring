package com.simplespring.ioc;

/**
 * 处理流程中BeanPostProcessor的环节
 */
public interface BeanPostProcessor {
    Object postProcessBeforeInitialization(Object bean,String beanName)throws Exception;
    Object postProcessAfterInitialization(Object bean,String beanName)throws Exception;
}
