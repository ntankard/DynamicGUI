package com.ntankard.dynamicGUI.CoreObject.Field.DataCore;

public class Static_DataCore<T> extends DataCore<T> {

    /**
     * The value the fired should always have
     */
    private final T value;

    /**
     * Constructor
     */
    public Static_DataCore(T value) {
        this.value = value;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public T get() {
        return value;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void set(T toSet) {
        throw new UnsupportedOperationException("Trying to set a static value");
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void initialSet(T toSet) {
        throw new UnsupportedOperationException("Trying to set a static value");
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canEdit() {
        return false;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canInitialSet() {
        return false;
    }
}
