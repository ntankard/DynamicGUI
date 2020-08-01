package com.ntankard.dynamicGUI.Gui.Components.List.Component.Renderer;

import com.ntankard.dynamicGUI.Gui.Components.List.DynamicGUI_DisplayTable_Model;

import javax.swing.*;
import java.awt.*;

public class NegativeHighlightRenderer extends Renderer {

    // Color constants
    private final static Color BELOW_ZERO = Color.RED;
    private final static Color ABOVE_ZERO = Color.GREEN;

    /**
     * If the value is above 0 should it be highlighted as well?
     */
    private final boolean highlightAbove;

    /**
     * Constructor
     *
     * @param highlightAbove If the value is above 0 should it be highlighted as well?
     */
    public NegativeHighlightRenderer(DynamicGUI_DisplayTable_Model parent, boolean highlightAbove) {
        super(parent);
        this.highlightAbove = highlightAbove;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Double) {
            Double number = (Double) value;
            if (number < 0.0) {
                cell.setBackground(BELOW_ZERO);
            } else {
                if (highlightAbove) {
                    cell.setBackground(ABOVE_ZERO);
                } else {
                    if (isSelected) {
                        cell.setBackground(table.getSelectionBackground());
                    } else {
                        cell.setBackground(table.getBackground());
                    }
                }
            }
        } else {
            throw new RuntimeException("Using a NegativeHighlightRenderer on an object type other than double");
        }

        return cell;
    }
}
