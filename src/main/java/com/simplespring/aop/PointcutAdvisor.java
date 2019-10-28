package com.simplespring.aop;

public interface PointcutAdvisor extends Advisor{
    Pointcut getPoint();
}
