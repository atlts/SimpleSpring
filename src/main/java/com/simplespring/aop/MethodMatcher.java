package com.simplespring.aop;

import java.lang.reflect.Method;

public interface MethodMatcher {
    Boolean matchers(Method method,Class beanClass);
}
