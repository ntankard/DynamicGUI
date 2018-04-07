package com.ntankard.DynamicGUI.Util.ExtendedSwing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class Extended_JTextField extends JTextField implements FocusListener {

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
    public Extended_JTextField() {
        this.addFocusListener(this);
        this.validColor = getBackground();
        this.invalidColor = Color.red;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void focusGained(FocusEvent e) {
    }

    /**
     * @inheritDoc
     */
    @Override
    public void focusLost(FocusEvent e) {
       /* if (validateState()) {
            setValid();
        } else {
            setInvalid();
        }*/
    }

    /**
     * Show to the user that the field is valid
     */
    public void setValid() {
        setBackground(validColor);
    }

    /**
     * Show to the user that the field is invalid
     */
    public void setInvalid() {
        setBackground(invalidColor);
    }
}


