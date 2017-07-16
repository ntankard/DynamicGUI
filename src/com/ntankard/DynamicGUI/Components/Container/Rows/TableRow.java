package com.ntankard.DynamicGUI.Components.Container.Rows;

import javax.swing.*;

/**
 * Created by Nicholas on 16/07/2017.
 */
public abstract class TableRow {

    /**
     * The name of the row (for a label)
     */
    public String name;

    /**
     * Is the component restricted
     */
    public boolean isRestricted;

    /**
     * Add this row to a standard Grid panel
     * @param toAddTo
     * @param index
     * @param isRestricted
     */
    public abstract void addToPanel(JPanel toAddTo, int index, boolean isRestricted);

    /**
     * Default constructor
     * @param name
     * @param isPrivate
     */
    public TableRow(String name, boolean isPrivate) {
        this.name = name;
        this.isRestricted = isPrivate;
    }
}