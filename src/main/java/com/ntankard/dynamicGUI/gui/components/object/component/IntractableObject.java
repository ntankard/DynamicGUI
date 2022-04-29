package com.ntankard.dynamicGUI.gui.components.object.component;

import com.ntankard.dynamicGUI.gui.util.containers.BufferedJPanel;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;

public abstract class IntractableObject<T> extends BufferedJPanel<T> {

    /**
     * The DataField that this panel is built around
     */
    protected DataField_Schema<T> dataFieldSchema;

    /**
     * The instance of the object
     */
    protected DataObject dataObject;

    /**
     * The display order, Integer.MAX_VALUE if none is set
     */
    private int order = Integer.MAX_VALUE;

    /**
     * Constructor
     */
    IntractableObject(DataField_Schema<T> dataFieldSchema, DataObject dataObject, boolean saveOnUpdate, int order, Updatable master) {
        super(saveOnUpdate, master);
        this.dataFieldSchema = dataFieldSchema;
        this.dataObject = dataObject;
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
    public DataField_Schema<T> getDataFieldSchema() {
        return dataFieldSchema;
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
     * @inheritDoc
     */
    @Override
    protected void revert() {
        load();
    }

    /**
     * @inheritDoc
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
        dataObject.set(getDataFieldSchema().getIdentifierName(), value);
    }

    protected T get() {
        return dataObject.get(getDataFieldSchema().getIdentifierName());
    }

    public DataObject getDataObject() {
        return dataObject;
    }
}
