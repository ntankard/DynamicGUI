package com.ntankard.DynamicGUI.Components.Object.Primitives.DataBinding;

import java.lang.reflect.Method;

/**
 * Created by Nicholas on 16/07/2017.
 */
public class BindableReflection<T> implements Bindable<T> {

    /**
     * The object to invoke
     */
    public Object o;

    /**
     * The getter method
     */
    public Method getter;

    /**
     * The setter method
     */
    public Method setter;

    /**
     * Should the setter method be called
     */
    private boolean canEdit;

    /**
     * Default constructor
     *
     * @param o       The object to invoke
     * @param getter  The getter method
     * @param setter  The setter method
     * @param canEdit Should the setter method be called
     */
    public BindableReflection(Object o, Method getter, Method setter, boolean canEdit) {
        this.o = o;
        this.getter = getter;
        this.setter = setter;
        this.canEdit = canEdit;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get() {
        try {
            return (T) getter.invoke(o, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void set(Object value) {
        try {
            setter.invoke(o, (T) value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canEdit() {
        return (setter != null && canEdit);
    }
}
