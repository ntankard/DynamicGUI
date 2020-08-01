package com.ntankard.dynamicGUI.Gui.Util.Containers;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {

    /**
     * The constraint used for the last JButton in the panel
     */
    private GridBagConstraints buttonC = null;

    /**
     * The primary button on the panel
     */
    private JButton mainBtn;

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
     * @param toAdd  The button to add
     * @param isMain Should this be the main button
     */
    public void addButton(JButton toAdd, boolean isMain) {
        if (buttonC == null) {
            createUIComponents();
        }

        if (isMain) {
            if (mainBtn != null) {
                throw new RuntimeException("Double setting the main btn");
            }
            mainBtn = toAdd;
        }

        buttonC.gridx++;
        this.add(toAdd, buttonC);
    }

    /**
     * Add a button to the panel
     *
     * @param toAdd The button to add
     */
    public void addButton(JButton toAdd) {
        addButton(toAdd, false);
    }

    /**
     * get the primary button on the panel
     *
     * @return The primary button on the panel
     */
    public JButton getMainBtn() {
        return mainBtn;
    }
}
