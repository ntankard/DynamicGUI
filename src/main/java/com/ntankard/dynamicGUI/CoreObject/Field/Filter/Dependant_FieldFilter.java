package com.ntankard.dynamicGUI.CoreObject.Field.Filter;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;

public abstract class Dependant_FieldFilter<T, ContainerType extends CoreObject> extends FieldFilter<T, ContainerType> {

    /**
     * The fields that this filter relies on to work, must have values before this filter is called
     */
    private final String[] requiredFields;

    /**
     * Constructor
     */
    public Dependant_FieldFilter(String... requiredFields) {
        this.requiredFields = requiredFields;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void attachedToField(DataField<T> field) {
        for (String requiredField : requiredFields) {
            field.addDependantField(requiredField);
        }
        super.attachedToField(field);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void detachedFromField(DataField<T> field) {
        super.detachedFromField(field);
        for (String requiredField : requiredFields) {
            field.removeDependantField(requiredField);
        }
    }
}
