package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

public class IntractableObject_Enum extends IntractableObject<Enum> implements ListSelectionListener {

    /**
     * The main list
     */
    private JList<Object> list;

    /**
     * Constructor
     *
     * @param baseMember   The member that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_Enum(ExecutableMember<Enum> baseMember, boolean saveOnUpdate, Updatable master) {
        super(baseMember, saveOnUpdate, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getBaseMember().getName()));
        this.setLayout(new BorderLayout());

        list = new JList<>(getBaseMember().getType().getEnumConstants());
        list.addListSelectionListener(this);
        JScrollPane listPane = new JScrollPane(list);

        this.add(listPane, BorderLayout.CENTER);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        valueChanged((Enum) list.getSelectedValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void load() {
        Enum current = getBaseMember().get();
        list.removeListSelectionListener(this);
        list.setSelectedValue(current, true);
        list.addListSelectionListener(this);
    }
}
