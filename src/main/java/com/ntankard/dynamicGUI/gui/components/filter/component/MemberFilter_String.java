package com.ntankard.dynamicGUI.gui.components.filter.component;

import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Predicate;

/**
 * A MemberFilter used to filter strings with either an exact, partial, or case sensitive match
 */
public class MemberFilter_String extends MemberFilter {

    /**
     * The value to match
     */
    private String value = "";

    /**
     * Should an exact match be used?
     */
    private boolean exactMatch = false;

    /**
     * Should the match be case sensitive
     */
    private boolean caseSensitive = false;

    /**
     * Constructor
     *
     * @param dataFieldSchema The DataField that this panel is built around
     * @param master    The top level GUI
     */
    public MemberFilter_String(DataField_Schema<?> dataFieldSchema, Updatable master) {
        super(dataFieldSchema, master);
        createUIComponents();
        update();
    }

    /**
     * Get the value of the object to compare to
     *
     * @param o The object to test
     * @return The invoked value
     */
    protected String getInstanceValue(DataObject o) {
        return (String) o.get(getDataFieldSchema().getIdentifierName());
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getDataFieldSchema().getDisplayName()));
        this.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.BOTH;

        JTextField value_txt = new JTextField();
        value_txt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String newValue = value_txt.getText();
                if (!value.equals(newValue)) {
                    value = newValue;
                    notifyUpdate();
                }
            }
        });
        c.gridwidth = 2;
        this.add(value_txt, c);

        JCheckBox caseSensitive_chb = new JCheckBox("Case?", caseSensitive);
        caseSensitive_chb.addActionListener(e -> {
            boolean newCaseSensitive = caseSensitive_chb.isSelected();
            if (caseSensitive != newCaseSensitive) {
                caseSensitive = newCaseSensitive;
                if (!value.isEmpty()) {
                    notifyUpdate();
                }
            }
        });
        c.gridy = 1;
        c.gridwidth = 1;
        this.add(caseSensitive_chb, c);

        JCheckBox exactMatch_chb = new JCheckBox("Exact?", exactMatch);
        exactMatch_chb.addActionListener(e -> {
            boolean newExactMatch = exactMatch_chb.isSelected();
            if (exactMatch != newExactMatch) {
                exactMatch = newExactMatch;
                if (!value.isEmpty()) {
                    notifyUpdate();
                }
            }
        });
        this.add(exactMatch_chb, c);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public Predicate<? extends DataObject> getPredicate() {
        return o -> {
            if (value.isEmpty()) {
                return true;
            }
            String readValue = getInstanceValue(o);
            if (readValue == null) {
                return value.isEmpty();
            }

            String expected = value;
            String actual = readValue;
            if (!caseSensitive) {
                expected = value.toUpperCase();
                actual = readValue.toUpperCase();
            }

            if (!exactMatch) {
                return actual.contains(expected);
            }
            return actual.equals(expected);
        };
    }
}
