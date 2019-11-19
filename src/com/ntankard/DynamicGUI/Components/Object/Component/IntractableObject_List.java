package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class IntractableObject_List extends IntractableObject<Object> implements ItemListener {

    /**
     * The main combo box
     */
    private JComboBox<Object> combo;

    /**
     * Constructor
     *
     * @param baseMember   The member that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_List(ExecutableMember<Object> baseMember, boolean saveOnUpdate, int order, Updatable master) {
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

        combo = new JComboBox<>();
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
    @SuppressWarnings("unchecked")
    protected void load() {
        Object current = getBaseMember().get();
        combo.removeItemListener(this);

        List<Object> options;
        try {
            options = (List<Object>) baseMember.getSource().invoke(baseMember.getObject(), baseMember.getType(), baseMember.getName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        combo.setModel(new DefaultComboBoxModel<>(options.toArray()));

        combo.setSelectedItem(current);
        combo.addItemListener(this);
    }
}
