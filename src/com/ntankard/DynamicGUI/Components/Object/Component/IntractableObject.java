package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Swing.Base.BufferedJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;

public abstract class IntractableObject<T> extends BufferedJPanel<T> {

    /**
     * The member that this panel is built around
     */
    protected ExecutableMember<T> baseMember;

    /**
     * The display order, Integer.MAX_VALUE if none is set
     */
    private int order = Integer.MAX_VALUE;

    /**
     * Constructor
     */
    IntractableObject(ExecutableMember<T> baseMember, boolean saveOnUpdate, int order, Updatable master) {
        super(saveOnUpdate, master);
        this.baseMember = baseMember;
        this.order = order;
    }

    /**
     * Update the GUI component from the bound data
     */
    protected abstract void load();

    /**
     * Get the member that this panel is built around
     *
     * @return The member that this panel is built around
     */
    protected ExecutableMember<T> getBaseMember() {
        return baseMember;
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
        getBaseMember().set(value);
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
}
