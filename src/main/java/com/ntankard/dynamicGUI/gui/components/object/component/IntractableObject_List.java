package com.ntankard.dynamicGUI.gui.components.object.component;

import com.ntankard.javaObjectDatabase.coreObject.field.DataField_Schema;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;

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
     * @param dataFieldSchema    The DataField that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_List(DataField_Schema<Object> dataFieldSchema, DataObject dataObject, boolean saveOnUpdate, int order, Updatable master) {
        super(dataFieldSchema, dataObject, saveOnUpdate, order, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    protected void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getDataFieldSchema().getDisplayName()));
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
        Object current = get();
        combo.removeItemListener(this);

        List<Object> options;
        try {
            options = (List<Object>) getDataFieldSchema().getSource().invoke(getDataObject(), getDataFieldSchema().getType(), getDataFieldSchema().getDisplayName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        combo.setModel(new DefaultComboBoxModel<>(options.toArray()));

        combo.setSelectedItem(current);
        combo.addItemListener(this);
    }
}
