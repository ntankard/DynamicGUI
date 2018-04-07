package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class IntractableObject_Enum extends IntractableObject implements ListSelectionListener {

    /**
     * The main list
     */
    private JList<Object> list;

    /**
     * {@inheritDoc}
     */
    public IntractableObject_Enum(Member member, Object toExecute, Updatable master) {
        super(member, toExecute, master);
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
    public void update() {
        try {
            Enum current = (Enum) getBaseMember().getGetter().invoke(getBaseInstance());
            list.removeListSelectionListener(this);
            list.setSelectedValue(current, true);
            list.addListSelectionListener(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        try {
            getBaseMember().getSetter().invoke(getBaseInstance(), list.getSelectedValue());
        } catch (IllegalAccessException | InvocationTargetException e1) {
            throw new RuntimeException(e1);
        }
        notifyUpdate();
    }
}