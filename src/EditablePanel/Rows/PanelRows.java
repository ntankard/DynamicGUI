package com.ntankard.DynamicGUI.Components.EditablePanel.Rows;

/**
 * Created by Nicholas on 16/07/2017.
 */

import com.intellij.uiDesigner.core.GridConstraints;
import com.ntankard.DynamicGUI.Components.EditablePanel.BoundComposite_JPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * A row containing a PanelManger
 */
public class PanelRows extends TableRow {

    /**
     * The object to create the panel
     */
    public BoundComposite_JPanel panel;

    /**
     * Default constructor
     *
     * @param name
     * @param isPrivate
     * @param panel
     */
    public PanelRows(String name, boolean isPrivate, BoundComposite_JPanel panel) {
        super(name, isPrivate);
        this.panel = panel;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void addToPanel(JPanel toAddTo, int index, boolean isRestricted) {
        panel_C.setRow(index);
        JPanel toAddP;
        if (isRestricted) {
            toAddP = panel.getRestrictedPanel();
        } else {
            toAddP = panel.getGeneralPanel();
        }
        toAddP.setBorder(new TitledBorder(name));
        toAddTo.add(toAddP, panel_C);
    }

    /**
     * The constraint for an object spanning both columns
     */
    public static final GridConstraints panel_C = new GridConstraints(
            0, 0, 1, 2,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null);
}
