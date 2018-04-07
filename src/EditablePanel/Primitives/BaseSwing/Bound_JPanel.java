package com.ntankard.DynamicGUI.Components.Object.Primitives.BaseSwing;

import javax.swing.*;

/**
 * Created by Nicholas on 16/07/2017.
 */
public abstract class Bound_JPanel extends JPanel implements Bound_JComponent {

    //------------------------------------------------------------------------------------------------------------------
    //################################# Util Bound_JComponent Implementation ########################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public JComponent getComponent() {
        return this;
    }
}
