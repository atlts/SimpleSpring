package com.simplespring.ioc;

/**
 * 存储每个属性的name和value
 */
public class PropertyValue {
    private final String name;
    private final Object value;

    public PropertyValue(String name ,Object value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
