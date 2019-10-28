package com.simplespring.aop;

public interface ClassFilter {
    Boolean matchers(Class beanClass) throws Exception;
}
