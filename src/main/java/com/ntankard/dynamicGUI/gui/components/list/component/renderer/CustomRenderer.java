package com.ntankard.dynamicGUI.gui.components.list.component.renderer;

import com.ntankard.dynamicGUI.gui.components.list.DynamicGUI_DisplayTable_Model;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties.ColorSelector;

import javax.swing.*;
import java.awt.*;

public class CustomRenderer extends Renderer {

    /**
     * The source of the color to set
     */
    private final ColorSelector colorSelector;

    /**
     * Constructor
     */
    public CustomRenderer(DynamicGUI_DisplayTable_Model parent, ColorSelector colorSelector) {
        super(parent);
        this.colorSelector = colorSelector;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Color toSet = colorSelector.getColor(rowObject, value);
        if (toSet != null) {
            cell.setBackground(toSet);
        } else {
            if (isSelected) {
                cell.setBackground(table.getSelectionBackground());
            } else {
                cell.setBackground(table.getBackground());
            }
        }

        return cell;
    }
}
