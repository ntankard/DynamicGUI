package com.ntankard.DynamicGUI;

import javax.swing.*;

/**
 * Created by Nicholas on 30/06/2016.
 */
public abstract class Bound_JButton extends JButton implements Bound {

    //------------------------------------------------------------------------------------------------------------------
    //####################################### General Bound Implementation #############################################
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
    public void disableAdmin(){}

    /**
     * @inheritDoc
     */
    @Override
    public void disableAdminSort(){}

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
