package com.ntankard.dynamicGUI.gui.components.object.component;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class IntractableObject_Enum extends IntractableObject<Enum<?>> implements ListSelectionListener {

    /**
     * The main list
     */
    private JList<Object> list;

    /**
     * Constructor
     *
     * @param dataFieldSchema The DataField that this panel is built around
     * @param saveOnUpdate    Should the action of the panel be done as soon as an update is received? or on command
     * @param master          The parent of this object to be notified if data changes
     */
    public IntractableObject_Enum(DataField_Schema<Enum<?>> dataFieldSchema, DataObject dataObject, boolean saveOnUpdate, int order, Updatable master) {
        super(dataFieldSchema, dataObject, saveOnUpdate, order, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getDataFieldSchema().getDisplayName()));
        this.setLayout(new BorderLayout());

        list = new JList<>(getDataFieldSchema().getType().getEnumConstants());
        list.addListSelectionListener(this);
        JScrollPane listPane = new JScrollPane(list);

        this.add(listPane, BorderLayout.CENTER);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        valueChanged((Enum<?>) list.getSelectedValue());
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void load() {
        Enum<?> current = get();
        list.removeListSelectionListener(this);
        list.setSelectedValue(current, true);
        list.addListSelectionListener(this);
    }
}
