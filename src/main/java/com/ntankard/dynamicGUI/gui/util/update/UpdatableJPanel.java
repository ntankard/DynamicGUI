package com.ntankard.dynamicGUI.gui.util.update;

import javax.swing.*;

/**
 * A JPanel with as much of the Updatable implementation as possible
 */
public abstract class UpdatableJPanel extends JPanel implements Updatable {

    /**
     * The parent of this object to be notified if data changes
     */
    private Updatable master;

    /**
     * Is there a general error on this panel (invalid data)
     */
    private boolean hasFault = false;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    protected UpdatableJPanel(Updatable master) {
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

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Getters ####################################################
    //------------------------------------------------------------------------------------------------------------------

    public boolean isHasFault() {
        return hasFault;
    }

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Setters ####################################################
    //------------------------------------------------------------------------------------------------------------------

    public void setHasFault(boolean hasFault) {
        this.hasFault = hasFault;
    }
}
