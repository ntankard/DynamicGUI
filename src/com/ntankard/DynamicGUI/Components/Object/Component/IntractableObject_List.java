package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.CoreObject.CoreObject;
import com.ntankard.CoreObject.Field.DataField;
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
     * @param dataField    The DataField that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_List(DataField<Object> dataField, CoreObject coreObject, boolean saveOnUpdate, int order, Updatable master) {
        super(dataField, coreObject, saveOnUpdate, order, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    protected void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getDataField().getDisplayName()));
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
            options = (List<Object>) getDataField().getSource().invoke(getCoreObject(), getDataField().getType(), getDataField().getDisplayName());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        combo.setModel(new DefaultComboBoxModel<>(options.toArray()));

        combo.setSelectedItem(current);
        combo.addItemListener(this);
    }
}
