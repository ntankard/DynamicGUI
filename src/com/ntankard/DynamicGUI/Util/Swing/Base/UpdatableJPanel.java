package com.ntankard.DynamicGUI.Util.Swing.Base;

import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.JPanel;

/**
 * A JPanel with as much of the Updatable implementation as possible
 */
public abstract class UpdatableJPanel extends JPanel implements Updatable {

    /**
     * The parent of this object to be notified if data changes
     */
    private Updatable master;

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
     * {@inheritDoc}
     */
    @Override
    public void notifyUpdate() {
        master.notifyUpdate();
    }
}
