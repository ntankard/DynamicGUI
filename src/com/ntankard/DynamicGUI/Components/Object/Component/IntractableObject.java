package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Swing.Base.BufferedJPanel;
import com.ntankard.DynamicGUI.Util.Updatable;

public abstract class IntractableObject<T> extends BufferedJPanel<T> {

    /**
     * The member that this panel is built around
     */
    private ExecutableMember<T> baseMember;

    /**
     * Constructor
     *
     * @param baseMember   The member that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param master       The parent of this object to be notified if data changes
     */
    IntractableObject(ExecutableMember<T> baseMember, boolean saveOnUpdate, Updatable master) {
        super(saveOnUpdate, master);
        this.baseMember = baseMember;
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
