package com.ntankard.CoreObject.Field.DataCore;

import com.ntankard.CoreObject.Field.DataField;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public abstract class DataCore<T> {

    /**
     * The field containing this data core
     */
    private DataField<T> dataField = null;

    //------------------------------------------------------------------------------------------------------------------
    //###################################################### Setup #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Called when this filter is attached to a field. It is safe to call subscriptions at this point
     *
     * @param dataField The field this object was attached to
     */
    public void attachToField(DataField<T> dataField) {
        this.dataField = dataField;
    }

    /**
     * Called when this filter is detached from a field. Clean up all attachments
     *
     * @param field The field this object was removed from
     */
    public void detachFromField(DataField<T> field) {
        this.dataField = null;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Data Access ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the current value
     *
     * @return The current value
     */
    public abstract T get();

    /**
     * Set the value of the field
     *
     * @param toSet The value to set
     */
    public abstract void set(T toSet);

    /**
     * Set the value of the field the first time. Can only be called once
     *
     * @param toSet The value to set
     */
    public abstract void initialSet(T toSet);

    //------------------------------------------------------------------------------------------------------------------
    //############################################### DataCore Properties ##############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Can the value be set after the initial set?
     *
     * @return True if the value be set after the initial set?
     */
    public abstract boolean canEdit();

    /**
     * Dose this DataCore contain core data? (Not derived from another field)
     *
     * @return True is this DataCore contain core data
     */
    public abstract boolean isDirectData();

    /**
     * Can the field be initially set?
     *
     * @return True if the field be initially set?
     */
    public boolean canInitialSet() {
        return true;
    }

    /**
     * Can this DataCore respond to changes in other fields and notify them when i change?
     *
     * @return True if this DataCore respond to changes in other fields and notify them when i change
     */
    public boolean doseSupportChangeListeners() {
        return true;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Standard Access #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the field containing this data core
     *
     * @return The field containing this data core
     */
    public DataField<T> getDataField() {
        return dataField;
    }
}