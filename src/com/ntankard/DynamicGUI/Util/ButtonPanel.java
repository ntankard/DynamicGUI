package com.ntankard.DynamicGUI.Util;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ButtonPanel extends JPanel {

    /**
     * All the buttons to add
     */
    private ArrayList<JButton> buttons = new ArrayList<>();

    /**
     * The constraint for a a button
     */
    private final GridConstraints button_C = new GridConstraints(
            0, 1, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null);

    /**
     * @param toAdd The button to add
     */
    public void addButton(JButton toAdd) {
        buttons.add(toAdd);
        updateButtonPanel();
    }

    /**
     * Finalize the panel by adding all the buttons
     */
    private void updateButtonPanel() {
        this.removeAll();
        this.setLayout(new GridLayoutManager(1, buttons.size(), new Insets(0, 0, 0, 0), -1, -1));
        if (buttons.size() != 0) {
            for (int i = 0; i < buttons.size(); i++) {
                button_C.setColumn(i);
                this.add(buttons.get(i), button_C);
            }
        }
    }
}
