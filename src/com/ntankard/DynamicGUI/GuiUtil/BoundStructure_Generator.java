package com.ntankard.DynamicGUI.GuiUtil;

import com.ntankard.DynamicGUI.Components.Container.BoundComposite_JPanel;

/**
 * Created by Nicholas on 26/06/2016.
 */
public interface BoundStructure_Generator {

    /**
     * Gets a panel with all the parameters available
     * @return
     */
    BoundComposite_JPanel getPanel();

    /**
     * A name representation of the object
     * @return
     */
    String toString();

    /**
     * Deep copy
     * @return
     */
    BoundStructure_Generator clone();
}
