package com.ntankard.DynamicGUI.GuiUtil;

/**
 * Created by Nicholas on 26/06/2016.
 */
public interface BoundStructure_Generator {

    /**
     * Gets a panel with all the parameters available
     * @return
     */
    // BoundComposite_JPanel getPanel();

    /**
     * A name representation of the object
     *
     * @return
     */
    String toString();

    String[] getHeaders();

    String[] getStringParts();

    /**
     * Deep copy
     * @return
     */
    // BoundStructure_Generator clone();
}
