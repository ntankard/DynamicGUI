package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IntractableObject_String extends IntractableObject<String> {

    /**
     * The displayed value
     */
    private String value = "";

    /**
     * The main text box
     */
    private JTextField value_txt;

    /**
     * {@inheritDoc}
     */
    public IntractableObject_String(ExecutableMember<String> member, Updatable master) {
        super(member, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createTitledBorder(getBaseMember().getName()));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;

        value_txt = new JTextField();
        value_txt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String newValue = value_txt.getText();
                if (!value.equals(newValue)) {
                    value = newValue;
                    notifyUpdate();
                }
            }
        });
        c.gridwidth = 1;
        this.add(value_txt, c);

        value_txt.setEditable(getBaseMember().canEdit());
        load();
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    void load() {
        String value = getBaseMember().get();
        value_txt.setText(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void save() {
        getBaseMember().set(value_txt.getText());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        load();
    }
}
