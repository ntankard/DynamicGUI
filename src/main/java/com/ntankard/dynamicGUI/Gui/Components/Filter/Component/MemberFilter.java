package com.ntankard.dynamicGUI.Gui.Components.Filter.Component;

import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.Gui.Util.Update.UpdatableJPanel;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;

import java.util.function.Predicate;

/**
 * Creates a predicate to filter a specific member based on GUI inputs
 */
public abstract class MemberFilter extends UpdatableJPanel {

    /**
     * The DataField that this panel is built around
     */
    private final DataField<?> dataField;

    /**
     * Constructor
     *
     * @param dataField The DataField that this panel is built around
     * @param master    The top level GUI
     */
    MemberFilter(DataField<?> dataField, Updatable master) {
        super(master);
        this.dataField = dataField;
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
    protected DataField<?> getDataField() {
        return dataField;
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
