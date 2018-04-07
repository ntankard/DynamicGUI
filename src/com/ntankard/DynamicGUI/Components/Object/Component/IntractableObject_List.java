package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class IntractableObject_List extends IntractableObject implements ItemListener {

    /**
     * The main combo box
     */
    private JComboBox<Object> combo = new JComboBox<>();

    /**
     * The possible options (must have toString)
     */
    private List options;

    /**
     * {@inheritDoc}
     */
    public IntractableObject_List(Member member, Object toExecute, List options, Updatable master) {
        super(member, toExecute, master);
        this.options = options;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(getBaseMember().getName()));

        combo = new JComboBox<>(options.toArray());
        combo.addItemListener(this);

        this.add(combo, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        try {
            Object current = getBaseMember().getGetter().invoke(getBaseInstance());
            combo.removeItemListener(this);
            combo.setSelectedItem(current);
            combo.addItemListener(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        try {
            getBaseMember().getSetter().invoke(getBaseInstance(), combo.getSelectedItem());
        } catch (IllegalAccessException | InvocationTargetException e1) {
            throw new RuntimeException(e1);
        }
        notifyUpdate();
    }
}