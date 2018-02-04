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
 * A MemberFilter_JPanel used to filter double with either an exact match
 */
public class Double_MemberFilter extends MemberFilter_JPanel {

    /**
     * The value to match
     */
    private Double value = null;

    // All components
    private JTextField value_txt;

    /**
     * {@inheritDoc}
     */
    public Double_MemberFilter(Member baseMember, Updatable master) {
        super(baseMember,master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(baseMember.getName()));

        value_txt = new JTextField();
        value_txt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                Double attempt = null;

                try {
                    attempt = Double.parseDouble(value_txt.getText());
                } catch (Exception ignored) {}

                if(attempt == null){
                    if(value != null){
                        value = attempt;
                        notifyUpdate();
                    }
                }else{
                    if(value == null || !attempt.equals(value)){
                        value = attempt;
                        notifyUpdate();
                    }
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
                return baseMember.getGetter().invoke(o).equals(value);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }
}