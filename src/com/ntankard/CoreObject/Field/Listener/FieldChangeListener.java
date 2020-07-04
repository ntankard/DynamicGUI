package com.ntankard.CoreObject.Field.Listener;

import com.ntankard.CoreObject.Field.DataField;

public interface FieldChangeListener<T> {

    /**
     * Called when the value is changed
     *
     * @param field    The field that changed
     * @param oldValue The past value
     * @param newValue The new value
     */
    void valueChanged(DataField<T> field, T oldValue, T newValue);
}
