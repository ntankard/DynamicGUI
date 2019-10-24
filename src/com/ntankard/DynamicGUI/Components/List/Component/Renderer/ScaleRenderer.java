package com.ntankard.DynamicGUI.Components.List.Component.Renderer;

import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayTable_Model;

import javax.swing.*;
import java.awt.*;

public class ScaleRenderer extends Renderer {

    /**
     * Constructor
     *
     * @param model The model used to generate the columns containing this render. Used to find min max
     */
    public ScaleRenderer(DynamicGUI_DisplayTable_Model model) {
        super(model);
    }

    /**
     * @inheritDoc
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof Double) {
            double number = (double) value;
            double max = getMax(column);
            double min = getMin(column);

            if (number == 0) {
                if (isSelected) {
                    cell.setBackground(table.getSelectionBackground());
                } else {
                    cell.setBackground(table.getBackground());
                }
            } else if (number > 0.0) {
                int scale = getScale(number, max);
                cell.setBackground(new Color(scale, 255, scale));
            } else {
                int scale = getScale(number, min);
                cell.setBackground(new Color(255, scale, scale));
            }
        } else {
            throw new RuntimeException("Using a ScaleRenderer on an object type other than double");
        }

        return cell;
    }

    /**
     * Get the max value of all the rows in this column
     *
     * @param column The column to get
     * @return The max value of all the rows in this column
     */
    private double getMax(int column) {
        double max = Double.MIN_VALUE;
        for (int row = 0; row < parent.getRowCount(); row++) {
            double value = (Double) parent.getValueAt(row, column);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * Get the min value of all the rows in this column
     *
     * @param column The column to get
     * @return The min value of all the rows in this column
     */
    private double getMin(int column) {
        double min = Double.MAX_VALUE;
        for (int row = 0; row < parent.getRowCount(); row++) {
            double value = (Double) parent.getValueAt(row, column);
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    /**
     * Get the color value scaled against the range of the column
     *
     * @param value The value
     * @param range The max/min value
     * @return The color value scaled against the range of the column
     */
    private int getScale(double value, double range) {
        double offset = 20;
        return (int) (((255 - offset) * (range - value)) / range);
    }
}
