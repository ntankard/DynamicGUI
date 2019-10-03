package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

public class IntractableObject_Double extends IntractableObject<Double> {

    /**
     * The main text box
     */
    protected JTextField value_txt;

    /**
     * The current displayed value to revert to if the user enters an invalid value
     */
    private Double currentValue = 0.0;

    /**
     * Constructor
     *
     * @param baseMember   The member that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_Double(ExecutableMember<Double> baseMember, boolean saveOnUpdate, int order, Updatable master) {
        super(baseMember, saveOnUpdate, order, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    protected void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getBaseMember().getName()));
        this.setLayout(new BorderLayout());

        value_txt = new JTextField();
        value_txt.setEditable(getBaseMember().canEdit());
        value_txt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    Double value;
                    if (!value_txt.getText().equals("-") && !value_txt.getText().equals("")) {
                        value = Double.parseDouble(value_txt.getText());
                        currentValue = value;
                    }
                    valueChanged(currentValue);
                } catch (Exception ignored) {
                    setValue(currentValue);
                }

            }
        });

        this.add(value_txt, BorderLayout.CENTER);
    }

    /**
     * Set the non 0 value
     *
     * @param value The value to set
     */
    protected void setValue(Double value) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        value_txt.setText(df2.format(value));
    }


    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected void load() {
        Double value = getBaseMember().get();
        if (value != null) {
            currentValue = getBaseMember().get();
            if (value == 0.0) {
                value_txt.setText("");
            } else {
                setValue(currentValue);
            }
        } else {
            value_txt.setText("");
        }
    }
}
