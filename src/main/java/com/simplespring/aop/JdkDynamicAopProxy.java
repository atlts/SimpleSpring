package com.simplespring.aop;

import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理的方法
 */
final public class JdkDynamicAopProxy extends AbstractAopProxy implements InvocationHandler {
    public JdkDynamicAopProxy(AdvisedSupport advised){
        super(advised);
    }
    /**
     * 为目标 bean 生成代理对象
     * @return bean 的代理对象
     */
    public Object getProxy() {
        return Proxy.newProxyInstance(getClass().getClassLoader(),advised.getTargetSource().getInterfaces(),this);//这里的类加载器和接口应该是同一个对象的吧
    }
    /**
     * InvocationHandler 接口中的 invoke 方法具体实现，封装了具体的代理逻辑
     * @param proxy
     * @param method
     * @param args
     * @return 代理方法或原方法的返回值
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodMatcher methodMatcher = advised.getMethodMatcher();
        //使用方法匹配器 methodMatcher 测试 bean 中原始方法 method 是否符合匹配规则
        if(methodMatcher != null && methodMatcher.matchers(method,advised.getTargetSource().getTargetClass())){
            // 获取 Advice。MethodInterceptor 的父接口继承了 Advice
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            /*
             * 将 bean 的原始方法 method 封装在 MethodInvocation 接口实现类对象中，
             * 并把生成的对象作为参数传给 Adivce 实现类对象，执行通知逻辑
             */
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(
                    advised.getTargetSource().getTarget(),method,args
            ));
        }else{
            // 当前 method 不符合匹配规则，直接调用 bean 的原始方法 method
            return method.invoke(advised.getTargetSource().getTarget(),args);
        }
    }
}
