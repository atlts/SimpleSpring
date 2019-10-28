package com.simplespring;

public class HelloServiceImp1 implements HelloService {
    public void sayHelloWorld() {
        System.out.println("hello world");
    }

    @Override
    public void sayAgain() {
        System.out.println("Once Again");
    }
}
