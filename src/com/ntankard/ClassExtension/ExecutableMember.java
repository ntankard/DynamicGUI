package com.ntankard.ClassExtension;

import java.lang.reflect.Method;

public class ExecutableMember<T> extends Member {

    /**
     * The object to execute on
     */
    private Object object;

    /**
     * Constructor
     *
     * @param context The base class the contains the member
     * @param getter  The getter that this member is based on
     * @param o       The object to execute on
     */
    public ExecutableMember(MemberClass context, Method getter, Object o) {
        super(context, getter);
        this.object = o;
    }

    /**
     * Get the object to execute on
     *
     * @return The object to execute on
     */
    public Object getObject() {
        return object;
    }

    /**
     * Set the value while maintaining the wrapper object
     *
     * @param value The value to set
     */
    public void set(T value) {
        try {
            getSetter().invoke(object, (T) value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the core object
     *
     * @return The core object
     */
    public T get() {
        try {
            return (T) getGetter().invoke(object, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Can set be called?
     *
     * @return Can set be called?
     */
    public boolean canEdit() {
        return canEdit;
    }
}
