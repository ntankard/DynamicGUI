package com.ntankard.DynamicGUI.Components.List.Component.Renderer;

import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayTable_Model;

import javax.swing.*;
import java.awt.*;

public class NonZeroRenderer extends Renderer {

    // Color constants
    private final static Color NON_ZERO = Color.RED;

    /**
     * Constructor
     */
    public NonZeroRenderer(DynamicGUI_DisplayTable_Model parent) {
        super(parent);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Double) {
            Double number = (Double) value;
            if (number != 0.0) {
                cell.setBackground(NON_ZERO);
            } else {
                if (isSelected) {
                    cell.setBackground(table.getSelectionBackground());
                } else {
                    cell.setBackground(table.getBackground());
                }
            }
        } else {
            throw new RuntimeException("Using a NonZeroRenderer on an object type other than double");
        }

        return cell;
    }
}
