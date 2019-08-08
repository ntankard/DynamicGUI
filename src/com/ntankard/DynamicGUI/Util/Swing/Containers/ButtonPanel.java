package com.ntankard.DynamicGUI.Util.Swing.Containers;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {

    /**
     * The constraint used for the last JButton in the panel
     */
    private GridBagConstraints buttonC = null;

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.setLayout(new GridBagLayout());

        buttonC = new GridBagConstraints();
        buttonC.weightx = 1;
        buttonC.fill = GridBagConstraints.BOTH;
    }

    /**
     * Add a button to the panel
     *
     * @param toAdd The button to add
     */
    public void addButton(JButton toAdd) {
        if (buttonC == null) {
            createUIComponents();
        }

        buttonC.gridx++;
        this.add(toAdd, buttonC);
    }
}
