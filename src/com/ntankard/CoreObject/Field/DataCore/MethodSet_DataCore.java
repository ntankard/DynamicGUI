package com.ntankard.CoreObject.Field.DataCore;

import com.ntankard.CoreObject.CoreObject;

public class MethodSet_DataCore<T> extends Method_DataCore<T> {

    /**
     * The method to get the value to set
     */
    private final Setter<T> setter;

    /**
     * Constructor
     */
    public MethodSet_DataCore(Getter<T> getter, Setter<T> setter) {
        super(getter);
        this.setter = setter;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void set(T toSet) {
        setter.set(getDataField().getContainer(), toSet);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean canEdit() {
        return true;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Interface Classes ###############################################
    //------------------------------------------------------------------------------------------------------------------

    public interface Setter<T> {
        void set(CoreObject container, T toSet);
    }
}