package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Updatable;

public abstract class IntractableObject<T> extends Updatable.UpdatableJPanel {

    /**
     * The member connected to this panel
     */
    private ExecutableMember<T> baseMember;

    /**
     * Constructor
     *
     * @param baseMember The member connected to this panel
     * @param master     The top level GUI
     */
    protected IntractableObject(ExecutableMember<T> baseMember, Updatable master) {
        super(master);
        this.baseMember = baseMember;
    }

    /**
     * Get the member connected to this panel
     *
     * @return The member connected to this panel
     */
    public ExecutableMember<T> getBaseMember() {
        return baseMember;
    }

    /**
     * Update the GUI component from the bound data
     */
    abstract void load();

    /**
     * Transcribe the value of the GUI object to the bound data
     */
    abstract void save();
}