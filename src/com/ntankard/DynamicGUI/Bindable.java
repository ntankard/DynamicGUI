package com.ntankard.DynamicGUI;

import java.io.Serializable;

/**
 * A wrapper for an object to allow it to be maintained even if it is overridden
 * @param <T>
 */
public class Bindable<T> implements Serializable {

    /**
     * The core object
     */
    private T value;

    /**
     * Default constructor
     * @param value
     */
    public Bindable(T value) {
        this.value = value;
    }

    /**
     * Set the value while maintaining the wrapper object
     * @param value
     */
    public void set(Object value){
        this.value = (T)value;
    }

    /**
     * Get the core object
     * @return
     */
    public T get(){
        return this.value;
    }
}