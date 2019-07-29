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
     * {@inheritDoc}
     */
    public IntractableObject_Enum(ExecutableMember<Enum> member, Updatable master) {
        super(member, master);
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

        list = new JList<>(getBaseMember().getType().getEnumConstants());
        list.addListSelectionListener(this);

        JScrollPane listPane = new JScrollPane(list);
        this.add(listPane, BorderLayout.CENTER);

        if (getBaseMember().getSetter() == null) {
            this.setEnabled(false);
            list.setEnabled(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
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
        Enum current = getBaseMember().get();
        list.removeListSelectionListener(this);
        list.setSelectedValue(current, true);
        list.addListSelectionListener(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void save() {
        getBaseMember().set((Enum)list.getSelectedValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        load();
    }
}