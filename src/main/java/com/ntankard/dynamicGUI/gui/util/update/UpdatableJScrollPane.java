package com.ntankard.dynamicGUI.gui.util.update;

import javax.swing.JScrollPane;

/**
 * A JScrollPane with as much of the Updatable implementation as possible
 */
public abstract class UpdatableJScrollPane extends JScrollPane implements Updatable {

    /**
     * The parent of this object to be notified if data changes
     */
    private Updatable master;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    protected UpdatableJScrollPane(Updatable master) {
        this.master = master;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void notifyUpdate() {
        master.notifyUpdate();
    }
}
