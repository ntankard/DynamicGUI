package com.ntankard.dynamicGUI.Gui.Components.List.Component.Renderer;

import com.ntankard.dynamicGUI.Gui.Components.List.DynamicGUI_DisplayTable_Model;

import javax.swing.*;
import java.awt.*;

public class FalseHighlightRenderer extends Renderer {

    // Color constants
    private final static Color NON_ZERO = Color.RED;

    /**
     * Constructor
     */
    public FalseHighlightRenderer(DynamicGUI_DisplayTable_Model parent) {
        super(parent);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Boolean) {
            Boolean aBoolean = (Boolean) value;
            if (!aBoolean) {
                cell.setBackground(NON_ZERO);
            } else {
                if (isSelected) {
                    cell.setBackground(table.getSelectionBackground());
                } else {
                    cell.setBackground(table.getBackground());
                }
            }
        } else {
            throw new RuntimeException("Using a BooleanRenderer on an object type other than Boolean");
        }

        return cell;
    }
}
