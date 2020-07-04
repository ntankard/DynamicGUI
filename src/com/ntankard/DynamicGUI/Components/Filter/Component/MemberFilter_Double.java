package com.ntankard.DynamicGUI.Components.Filter.Component;

import com.ntankard.CoreObject.CoreObject;
import com.ntankard.CoreObject.Field.DataField;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

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
     * @param dataField The DataField that this panel is built around
     * @param master    The top level GUI
     */
    public MemberFilter_Double(DataField<?> dataField, Updatable master) {
        super(dataField, master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getDataField().getDisplayName()));
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
    public Predicate<? extends CoreObject> getPredicate() {
        return o -> {
            if (value == null) {
                return true;
            }
            return o.get(getDataField().getIdentifierName()).equals(value);
        };
    }
}
