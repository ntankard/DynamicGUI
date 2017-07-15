package com.ntankard.DynamicGUI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Nicholas on 14/07/2017.
 */
public class EditableBindable<T> extends Bindable<T> {
    public Method get;
    public Method setter;
    public Object o;


    public EditableBindable(Object o, Method get, Method setter) throws InvocationTargetException, IllegalAccessException {
        super((T)get.invoke(o,null));
        this.get = get;
        this.o = o;
        this.setter = setter;
    }

    @Override
    public T get() {
        try {
            return (T)get.invoke(o,null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void set(Object value) {
        try {
            setter.invoke(o,(T)value);
            System.out.println("Yesy");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
