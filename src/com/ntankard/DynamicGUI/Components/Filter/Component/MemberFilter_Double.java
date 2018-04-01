package com.ntankard.DynamicGUI.Components.Filter.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Predicate;

/**
 * A MemberFilter used to filter double with either an exact match
 */
public class MemberFilter_Double extends MemberFilter {

    /**
     * The value to match
     */
    private Double value = null;

    /**
     * {@inheritDoc}
     */
    public MemberFilter_Double(Member baseMember, Updatable master) {
        super(baseMember, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(getBaseMember().getName()));

        JTextField value_txt = new JTextField();
        value_txt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                Double old = value;

                try {
                    value = Double.parseDouble(value_txt.getText());
                } catch (Exception ignored) {
                    value = null;
                }

                if (old != value) {
                    notifyUpdate();
                }
            }
        });
        this.add(value_txt, BorderLayout.CENTER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Predicate getPredicate() {
        return o -> {
            if (value == null) {
                return true;
            }
            try {
                return getBaseMember().getGetter().invoke(o).equals(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }
}