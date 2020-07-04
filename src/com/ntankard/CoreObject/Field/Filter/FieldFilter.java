package com.ntankard.CoreObject.Field.Filter;

import com.ntankard.CoreObject.CoreObject;
import com.ntankard.CoreObject.Field.DataField;

/**
 * @param <T>             The type of object that will need to be checked
 * @param <ContainerType> The type of the container of the field that houses this filter
 */
public abstract class FieldFilter<T, ContainerType extends CoreObject> {

    /**
     * Check that a value is valid fora  given field
     *
     * @param value     The value to check
     * @param container The object that contains the field this filter is attached to
     * @return True if the value is valid
     */
    public abstract boolean isValid(T value, ContainerType container);

    /**
     * Called when this filter is attached to a field, run all required attachments now
     *
     * @param field the field i was attached to
     */
    public void attachedToField(DataField<T> field) {
    }

    /**
     * Called when this filter is detached from a field. Clean up all attachments
     *
     * @param field The field i was removed from
     */
    public void detachedFromField(DataField<T> field) {
    }
}