package com.ntankard.dynamicGUI.gui.components.filter.component;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJPanel;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;

import java.util.function.Predicate;

/**
 * Creates a predicate to filter a specific member based on GUI inputs
 */
public abstract class MemberFilter extends UpdatableJPanel {

    /**
     * The DataField that this panel is built around
     */
    private final DataField_Schema<?> dataFieldSchema;

    /**
     * Constructor
     *
     * @param dataFieldSchema The DataField that this panel is built around
     * @param master          The top level GUI
     */
    MemberFilter(DataField_Schema<?> dataFieldSchema, Updatable master) {
        super(master);
        this.dataFieldSchema = dataFieldSchema;
    }

    /**
     * Get the predicate generated from this panels content
     *
     * @return The predicate generated from this panels content
     */
    public abstract Predicate getPredicate();

    /**
     * Get the DataField that this panel is built around
     *
     * @return The DataField that this panel is built around
     */
    protected DataField_Schema<?> getDataFieldSchema() {
        return dataFieldSchema;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void update() {
    }
}
