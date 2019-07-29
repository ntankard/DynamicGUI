package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

public class IntractableObject_List extends IntractableObject<Object> implements ItemListener {

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
    public IntractableObject_List(ExecutableMember<Object> member, List options, Updatable master) {
        super(member, master);
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
    public void itemStateChanged(ItemEvent e) {
        save();
        notifyUpdate();
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    void load() {
        Object current = getBaseMember().get();
        combo.removeItemListener(this);
        combo.setSelectedItem(current);
        combo.addItemListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void save() {
        getBaseMember().set(combo.getSelectedItem());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        load();
    }
}