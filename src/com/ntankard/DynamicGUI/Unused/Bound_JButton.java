package com.ntankard.DynamicGUI.Unused;

import com.ntankard.DynamicGUI.Components.BaseSwing.Bound_JComponent;

import javax.swing.*;

/**
 * Created by Nicholas on 30/06/2016.
 */
public abstract class Bound_JButton extends JButton implements Bound_JComponent {

    //------------------------------------------------------------------------------------------------------------------
    //####################################### General Bound_JComponent Implementation #############################################
    //------------------------------------------------------------------------------------------------------------------

    public Bound_JButton(String name){
        super(name);
    }

    /**
     * @inheritDoc
     */
    @Override
    public JComponent getComponent() {
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void load(){}

    /**
     * @inheritDoc
     */
    @Override
    public void save(){}

    /**
     * @inheritDoc
     */
    @Override
    public boolean validateState(){return true;}
}
