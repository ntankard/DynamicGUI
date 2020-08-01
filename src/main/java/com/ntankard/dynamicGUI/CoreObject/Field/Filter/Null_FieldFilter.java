package com.ntankard.dynamicGUI.CoreObject.Field.Filter;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;

public class Null_FieldFilter<T, ContainerType extends CoreObject> extends FieldFilter<T, ContainerType> {

    /**
     * Can the field be set to null?
     */
    private final Boolean canBeNull;

    /**
     * Constructor
     */
    public Null_FieldFilter(Boolean canBeNull) {
        this.canBeNull = canBeNull;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public boolean isValid(T value, ContainerType container) {
        if (!canBeNull) {
            return value != null;
        }
        return true;
    }
}
