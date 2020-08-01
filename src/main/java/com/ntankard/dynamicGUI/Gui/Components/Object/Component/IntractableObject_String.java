package com.ntankard.dynamicGUI.Gui.Components.Object.Component;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.Gui.Util.Decoder.Decoder;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
     * @param dataField The DataField that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param order        The order of this object
     * @param decoder      The decoder used to convert to and from a string
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_String(DataField<T> dataField, CoreObject coreObject, boolean saveOnUpdate, int order, Decoder<T> decoder, Updatable master) {
        super(dataField, coreObject, saveOnUpdate, order, master);
        this.decoder = decoder;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    protected void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getDataField().getDisplayName()));
        this.setLayout(new BorderLayout());

        value_txt = new JTextField();
        value_txt.setEditable(getDataField().getDataCore().canEdit());
        value_txt.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
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
        Object value = get();
        if (value != null) {
            currentValue = get();
            value_txt.setText(decoder.decode(currentValue, getCoreObject()));
        } else {
            value_txt.setText("");
        }
    }
}
