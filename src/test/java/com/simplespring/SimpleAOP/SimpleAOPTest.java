package com.simplespring.SimpleAOP;

import com.simplespring.HelloService;
import com.simplespring.HelloServiceImp1;
import org.junit.Test;

public class SimpleAOPTest {
    @Test
    public void testAOP(){
        MethodInvocation logTask = new MethodInvocation() {
            public void invoke() {
                System.out.println("start test");
            }
        };
        HelloServiceImp1 helloServiceImp1 = new HelloServiceImp1();
        Advice beforeAdvice = new BeforeAdvice(helloServiceImp1,logTask);
        HelloService helloServiceImp1Proxy = (HelloService)SimpleAOP.getProxy(helloServiceImp1,beforeAdvice);
        helloServiceImp1Proxy.sayHelloWorld();
    }
}
