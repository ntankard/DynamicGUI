package com.ntankard.dynamicGUI.Gui.Components.Filter.Component;

import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
     * Constructor
     *
     * @param dataFieldSchema The DataField that this panel is built around
     * @param master    The top level GUI
     */
    public MemberFilter_Double(DataField_Schema<?> dataFieldSchema, Updatable master) {
        super(dataFieldSchema, master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getDataFieldSchema().getDisplayName()));
        this.setLayout(new BorderLayout());

        JTextField value_txt = new JTextField();
        value_txt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                Double old = value;

                try {
                    value = Double.parseDouble(value_txt.getText());
                } catch (Exception ignored) {
                    value = null;
                    value_txt.setText("");
                }

                if ((old == null && value != null) || (old != null && !old.equals(value))) {
                    notifyUpdate();
                }
            }
        });

        this.add(value_txt, BorderLayout.CENTER);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Predicate<? extends DataObject> getPredicate() {
        return o -> {
            if (value == null) {
                return true;
            }
            return o.get(getDataFieldSchema().getIdentifierName()).equals(value);
        };
    }
}
