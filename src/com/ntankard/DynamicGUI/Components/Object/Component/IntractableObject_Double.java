package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IntractableObject_Double extends IntractableObject<Double> {

    /**
     * The main text box
     */
    private JTextField value_txt;

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
    public IntractableObject_Double(ExecutableMember<Double> baseMember, boolean saveOnUpdate, Updatable master) {
        super(baseMember, saveOnUpdate, master);
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
                    Double value = Double.parseDouble(value_txt.getText());
                    currentValue = value;
                    valueChanged(currentValue);
                } catch (Exception ignored) {
                    value_txt.setText(currentValue.toString());
                }

            }
        });

        this.add(value_txt, BorderLayout.CENTER);
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
            value_txt.setText(currentValue.toString());
        } else {
            value_txt.setText("");
        }
    }
}
