package com.ntankard.DynamicGUI.Generator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectField {

    private Object o;
    private String fieldName;

    private Field field;
    private Method getter;
    private Method setter;

    public ObjectField(Object o, String fieldName, Field field, Method getter, Method setter) {
        this.o = o;
        this.fieldName = fieldName;
        this.field = field;
        this.getter = getter;
        this.setter = setter;
    }

    public Object getO() {
        return o;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Field getField() {
        return field;
    }

    public Method getGetter() {
        return getter;
    }

    public Method getSetter() {
        return setter;
    }
}
