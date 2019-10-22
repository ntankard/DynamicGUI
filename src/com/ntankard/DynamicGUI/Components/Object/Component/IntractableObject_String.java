package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Util.Decoder.Decoder;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class IntractableObject_String<T> extends IntractableObject<T> {

    /**
     * The main text box
     */
    private JTextField value_txt;

    /**
     * The current displayed value to revert to if the user enters an invalid value
     */
    private T currentValue;

    /**
     * The decoder used to convert to and from a string
     */
    protected Decoder<T> decoder;

    /**
     * Constructor
     *
     * @param baseMember   The member that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param order        The order of this object
     * @param decoder      The decoder used to convert to and from a string
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_String(ExecutableMember<T> baseMember, boolean saveOnUpdate, int order, Decoder<T> decoder, Updatable master) {
        super(baseMember, saveOnUpdate, order, master);
        this.decoder = decoder;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    protected void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getBaseMember().getName()));
        this.setLayout(new BorderLayout());

        value_txt = new JTextField();
        value_txt.setEditable(getBaseMember().canEdit());
        value_txt.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    currentValue = decoder.encode(value_txt.getText());
                    valueChanged(currentValue);
                } catch (Exception ignored) {
                    value_txt.setText(decoder.decode(currentValue, null));
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
    protected void load() {
        Object value = getBaseMember().get();
        if (value != null) {
            currentValue = getBaseMember().get();
            value_txt.setText(decoder.decode(currentValue, getBaseMember().getObject()));
        } else {
            value_txt.setText("");
        }
    }
}
