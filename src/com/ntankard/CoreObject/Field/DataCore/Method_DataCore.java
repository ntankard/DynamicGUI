package com.ntankard.CoreObject.Field.DataCore;

import com.ntankard.CoreObject.CoreObject;

public class Method_DataCore<T> extends Calculate_DataCore<T> {

    /**
     * The method to get the value
     */
    private final Getter<T> getter;

    /**
     * Constructor
     */
    public Method_DataCore(Getter<T> getter) {
        this.getter = getter;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public T get() {
        return getter.get(getDataField().getContainer());
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Interface Classes ###############################################
    //------------------------------------------------------------------------------------------------------------------

    public interface Getter<T> {
        T get(CoreObject container);
    }
}