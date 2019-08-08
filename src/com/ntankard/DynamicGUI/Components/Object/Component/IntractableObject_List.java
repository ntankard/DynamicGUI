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
    private JComboBox<Object> combo;

    /**
     * The possible options (must have toString)
     */
    private List options;

    /**
     * Constructor
     *
     * @param baseMember   The member that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param options      The values that can be selected
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_List(ExecutableMember<Object> baseMember, boolean saveOnUpdate, List options, Updatable master) {
        super(baseMember, saveOnUpdate, master);
        this.options = options;
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

        combo = new JComboBox<>(options.toArray()); // @TODO this can change over time, it should be updated in update
        combo.addItemListener(this);

        this.add(combo, BorderLayout.CENTER);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        valueChanged(combo.getSelectedItem());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void load() {
        Object current = getBaseMember().get();
        combo.removeItemListener(this);
        combo.setSelectedItem(current);
        combo.addItemListener(this);
    }
}
