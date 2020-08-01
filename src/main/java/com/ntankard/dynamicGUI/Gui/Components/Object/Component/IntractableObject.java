package com.ntankard.dynamicGUI.Gui.Components.Object.Component;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.Gui.Util.Containers.BufferedJPanel;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;

public abstract class IntractableObject<T> extends BufferedJPanel<T> {

    /**
     * The DataField that this panel is built around
     */
    protected DataField<T> dataField;

    /**
     * The instance of the object
     */
    protected CoreObject coreObject;

    /**
     * The display order, Integer.MAX_VALUE if none is set
     */
    private int order = Integer.MAX_VALUE;

    /**
     * Constructor
     */
    IntractableObject(DataField<T> dataField, CoreObject coreObject, boolean saveOnUpdate, int order, Updatable master) {
        super(saveOnUpdate, master);
        this.dataField = dataField;
        this.coreObject = coreObject;
        this.order = order;
    }

    /**
     * Update the GUI component from the bound data
     */
    protected abstract void load();

    /**
     * Get the DataField that this panel is built around
     *
     * @return The DataField that this panel is built around
     */
    public DataField<T> getDataField() {
        return dataField;
    }

    /**
     * Get the display order, Integer.MAX_VALUE if none is set
     *
     * @return The display order, Integer.MAX_VALUE if none is set
     */
    public int getOrder() {
        return order;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected void revert() {
        load();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void executeValue(T value) {
        set(value);
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void update() {
        if (!isEdited()) {
            load();
        }
    }

    protected void set(T value) {
        coreObject.set(getDataField().getIdentifierName(), value);
    }

    protected T get() {
        return coreObject.get(getDataField().getIdentifierName());
    }

    public CoreObject getCoreObject() {
        return coreObject;
    }
}
