package com.ntankard.DynamicGUI.DataBinding;

/**
 * A wrapper for an object to allow it to be maintained even if it is overridden
 * @param <T>
 */
public interface Bindable<T> {

    /**
     * Set the value while maintaining the wrapper object
     * @param value
     */
    void set(Object value);

    /**
     * Get the core object
     * @return The core object
     */
    T get();

    /**
     * Can set be called?
     * @return Can set be called?
     */
    boolean canEdit();
}