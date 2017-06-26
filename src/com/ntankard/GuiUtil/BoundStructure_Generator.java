package com.ntankard.GuiUtil;

import com.ntankard.DynamicGUI.PanelManager;

/**
 * Created by Nicholas on 26/06/2016.
 */
public interface BoundStructure_Generator {

    /**
     * Gets a panel with all the parameters available
     * @return
     */
    PanelManager getPanel();

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
