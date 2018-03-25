package com.ntankard.ClassExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A real or virtual method on a class signified by the fact that there is a getter available for it
 */
public class Member {

    /**
     * The type of the field (type of the getter returns and setter set)
     */
    private Class<?> type;

    /**
     * The suspected member name
     */
    private String name;

    /**
     * The member itself if its not virtual (can be null)
     */
    private Field field;

    /**
     * The getter that the member is derived from
     */
    private Method getter;

    /**
     * The setter if it is available (can be null)
     */
    private Method setter;

    /**
     * Constructor
     *
     * @param context The base class the contains the member
     * @param getter  The getter that this member is based on
     */
    public Member(MemberClass context, Method getter) {
        this.getter = getter;
        this.name = getter.getName().replace("get", "").replace("is", "").replace("has", "");
        this.type = getGetter().getReturnType();

        try {
            this.setter = context.getMethod("set" + name, type);
        } catch (NoSuchMethodException ignored) {
        }

        try {
            this.field = context.getField(name.substring(0, 1).toLowerCase() + name.substring(1));
        } catch (NoSuchFieldException ignored) {
        }

        // verify the getter and setter
        if (setter != null) {
            if (setter.getParameterCount() != 1 || !type.equals(setter.getParameterTypes()[0])) {
                throw new RuntimeException("Types don't match");
            }
        }
    }

    public String getName() {
        return name;
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
    public Class<?> getType(){
        return type;
    }
}