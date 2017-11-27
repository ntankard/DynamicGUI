package com.ntankard.DynamicGUI.Components.BaseSwing;

import javax.swing.*;

/**
 * Wrapper for JComponents that allow them to be updated
 */
public interface Bound_JComponent {

    /**
     * Update the GUI component from the bound data
     */
    void load();

    /**
     * Transcribe the value of the GUI object to the bound data
     */
    void save();

    /**
     * Is the value of the field valid (should set a warning on the field)
     *
     * @return
     */
    boolean validateState();

    //------------------------------------------------------------------------------------------------------------------
    //############################################ JComponent options ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the GUI Component (this)
     *
     * @return
     */
    JComponent getComponent();
}