package com.ntankard.DynamicGUI.Components.Container.Rows;

/**
 * Created by Nicholas on 16/07/2017.
 */

import com.intellij.uiDesigner.core.GridConstraints;
import com.ntankard.DynamicGUI.Components.BaseSwing.Bound_JComponent;

import javax.swing.*;

/**
 * A row containing a label and a JComponent
 */
public class BoundRows extends TableRow {

    /**
     * The main data component
     */
    public Bound_JComponent data;

    /**
     * Default constructor
     *
     * @param name
     * @param isPrivate
     * @param data
     */
    public BoundRows(String name, boolean isPrivate, Bound_JComponent data) {
        super(name, isPrivate);
        this.data = data;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addToPanel(JPanel toAddTo, int index, boolean isRestricted) {
        lbl_C.setRow(index);
        data_C.setRow(index);
        toAddTo.add(new JLabel(name), lbl_C);
        toAddTo.add(data.getComponent(), data_C);
    }

    /**
     * The constraint for a label in the left column
     */
    public static final GridConstraints lbl_C = new GridConstraints(
            0, 0, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null);

    /**
     * The constraint for a data element in the right column
     */
    public static final GridConstraints data_C = new GridConstraints(
            0, 1, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null);
}

