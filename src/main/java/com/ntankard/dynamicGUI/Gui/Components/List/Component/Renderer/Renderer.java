package com.ntankard.dynamicGUI.Gui.Components.List.Component.Renderer;

import com.ntankard.dynamicGUI.Gui.Util.Decoder.Decoder;
import com.ntankard.dynamicGUI.Gui.Components.List.DynamicGUI_DisplayTable_Model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Renderer extends DefaultTableCellRenderer {

    /**
     * The decoder used to get the display string
     */
    private Decoder decoder = null;

    /**
     * The row of the last cell to fire getTableCellRendererComponent
     */
    private Object rowObject = null;

    /**
     * The model containing this render, used to get row data
     */
    protected DynamicGUI_DisplayTable_Model parent;

    /**
     * Constructor
     *
     * @param parent The model containing this render, used to get row data
     */
    public Renderer(DynamicGUI_DisplayTable_Model parent) {
        this.parent = parent;
    }

    /**
     * Set the decoder used to get the display string
     *
     * @param decoder the decoder used to get the display string
     */
    public void setDecoder(Decoder decoder) {
        this.decoder = decoder;
    }

    /**
     * Get the decoder used to get the display string
     *
     * @return The decoder used to get the display string
     */
    public Decoder getDecoder() {
        return decoder;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        rowObject = parent.getRowObject(row);
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected void setValue(Object value) {
        if (decoder != null) {
            super.setValue(decoder.decode(value, rowObject));
        } else {
            super.setValue(value);
        }
    }
}
