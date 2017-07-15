package com.ntankard.DynamicGUI;

import com.ntankard.DynamicGUI.Bindable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Nicholas on 14/07/2017.
 */
public class NonEditableBindable<T> extends Bindable<T> {
    public Method get;
    public Object o;


    public NonEditableBindable(Object o, Method get) throws InvocationTargetException, IllegalAccessException {
        super((T)get.invoke(o,null));
        this.get = get;
        this.o = o;
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

    }

    @Override
    public boolean canEdit() {
        return false;
    }
}
