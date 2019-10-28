package com.simplespring.aop;

import com.simplespring.HelloService;
import com.simplespring.HelloServiceImp1;
import org.junit.Test;

import  java.lang.reflect.Method;
public class JdkDynamicAopProxyTest {
    @Test
    public void getProxy() throws Exception{
        System.out.println("NO Proxy===================");
        HelloService helloService = new HelloServiceImp1();
        helloService.sayHelloWorld();

        System.out.println("Proxy=================");
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setMethodInterceptor(new LogInterceptor());
        TargetSource targetSource = new TargetSource(
                helloService,HelloServiceImp1.class,HelloServiceImp1.class.getInterfaces()
        );
        advisedSupport.setTargetSource(targetSource);
        /**
         * 这应该是用了一个lambda表达式，表示beanClass中的方法全都有用
         */
        advisedSupport.setMethodMatcher((Method method,Class beanClass) -> true);
        helloService = (HelloService)new JdkDynamicAopProxy(advisedSupport).getProxy();
        helloService.sayHelloWorld();
        helloService.sayAgain();
    }
}
