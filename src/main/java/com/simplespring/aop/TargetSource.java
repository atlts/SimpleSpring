package com.simplespring.aop;

/**
 * 目标资源分别存储相应的对象，和目标对象的类和目标对象类实现的接口
 */
public class TargetSource {
    private Class<?>targetClass;
    private Class<?>[]interfaces;
    private Object target;

    public TargetSource(Object target,Class<?> targetClass, Class<?>... interfaces) {
        this.targetClass = targetClass;
        this.interfaces = interfaces;
        this.target = target;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Class<?>[] getInterfaces() {
        return interfaces;
    }

    public Object getTarget() {
        return target;
    }
}
