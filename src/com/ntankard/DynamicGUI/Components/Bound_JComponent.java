package com.ntankard.DynamicGUI.Components;

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
     * @return
     */
    boolean validateState();

    //------------------------------------------------------------------------------------------------------------------
    //############################################ JComponent options ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the GUI Component (this)
     * @return
     */
    JComponent getComponent();

    //------------------------------------------------------------------------------------------------------------------
    //############################################ Panel only options ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Don't put an admin override on the panel
     */
    void disableAdmin();

    /**
     * Prevent the separation of admin and non admin components (order by insert)
     */
     void disableAdminSort();
}