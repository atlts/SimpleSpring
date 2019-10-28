package com.simplespring.ioc;

import java.util.ArrayList;
import java.util.List;

public class PropertyValues {
    private final List<PropertyValue> propertyValueList = new ArrayList<PropertyValue>();

    /**
     * 不直接使用一个ArrayList而是创建类是为了将pv加进来时可以进行一些想要的操作
     * @param pv
     */
    public void addPropertyValue(PropertyValue pv){
        this.propertyValueList.add(pv);
    }

    public List<PropertyValue>getPropertyValues(){
        return this.propertyValueList;
    }
}
