package com.ntankard.DynamicGUI.Components.BaseSwing;

import javax.swing.*;

/**
 * Created by Nicholas on 16/07/2017.
 */
public abstract class Bound_JPanel extends JPanel implements Bound_JComponent {

    //------------------------------------------------------------------------------------------------------------------
    //################################# General Bound_JComponent Implementation ########################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public JComponent getComponent() {
        return this;
    }
}
