package com.simplespring.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 这还是个围绕通知呢
 */
public class LogInterceptor implements MethodInterceptor {
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println(invocation.getMethod().getName() + " start");
        System.out.println(invocation.getMethod().getName() + " end");
        Object obj = invocation.proceed();
        return obj;
    }
}
