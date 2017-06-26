package com.ntankard.DynamicGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Created by Nicholas on 8/06/2016.
 */
public abstract class Bound_JComboBox extends JComboBox implements Bound,FocusListener {

    /**
     * The default color of the field
     */
    private Color validColor;

    /**
     * The invalid color of the field
     */
    private Color invalidColor;

    /**
     * Default constructor
     */
    public Bound_JComboBox(){
        this.addFocusListener(this);
        this.validColor = getBackground();
        this.invalidColor = Color.red;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void focusGained(FocusEvent e) { }

    /**
     * @inheritDoc
     */
    @Override
    public void focusLost(FocusEvent e) {
        if(validateState()){
            setValid();
        }else{
            setInvalid();
        }
    }

    /**
     * Show to the user that the field is valid
     */
    public void setValid(){
        setBackground(validColor);
    }

    /**
     * Show to the user that the field is invalid
     */
    public void setInvalid(){
        setBackground(invalidColor);
    }

    //------------------------------------------------------------------------------------------------------------------
    //####################################### General Bound Implementation #############################################
    //------------------------------------------------------------------------------------------------------------------

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
}