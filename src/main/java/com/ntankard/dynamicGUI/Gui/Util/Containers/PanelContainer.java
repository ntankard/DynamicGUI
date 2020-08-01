package com.ntankard.dynamicGUI.Gui.Util.Containers;

import com.ntankard.dynamicGUI.Gui.Util.Update.UpdatableJPanel;
import com.ntankard.dynamicGUI.Gui.Util.Update.UpdatableJScrollPane;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PanelContainer extends UpdatableJScrollPane {

    /**
     * The main panel in the scroll pane
     */
    private JPanel viewportPanel;

    /**
     * The separator at the bottom to ensure everything stays at the top
     */
    private JSeparator separator = new JSeparator();

    /**
     * The constraint used for the last MemberPanel in the panel
     */
    private GridBagConstraints componentC;

    /**
     * All panels stores so they can be updates
     */
    private List<UpdatableJPanel> panels = new ArrayList<>();

    /**
     * Arrange the panel vertically?
     */
    private boolean vertical;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    protected PanelContainer(Updatable master) {
        this(true, master);
    }

    protected PanelContainer(boolean vertical, Updatable master) {
        super(master);
        this.vertical = vertical;
    }

    /**
     * Create the GUI components
     */
    protected void createUIComponents() {
        if (vertical) {
            viewportPanel = new JPanel(new GridBagLayout());
        } else {
            viewportPanel = new JPanel();
        }

        componentC = new GridBagConstraints();
        componentC.weightx = 1;
        componentC.fill = GridBagConstraints.BOTH;
        componentC.gridy = 0;

        GridBagConstraints spacerC = new GridBagConstraints();
        spacerC.weighty = 1;
        spacerC.gridy = 0;
        if (vertical) {
            viewportPanel.add(separator, spacerC);
        }

        this.setViewportView(viewportPanel);
    }

    /**
     * Add a MemberPanel to the panel
     *
     * @param memberPanel The panel to add
     */
    protected void addMember(UpdatableJPanel memberPanel) {
        if (vertical) {
            viewportPanel.remove(separator);
        }

        componentC.gridy++;
        if (vertical) {
            viewportPanel.add(memberPanel, componentC);
        } else {
            viewportPanel.add(memberPanel);
        }
        panels.add(memberPanel);

        GridBagConstraints spacerC = new GridBagConstraints();
        spacerC.weighty = 1;
        spacerC.gridy = componentC.gridy + 1;
        if (vertical) {
            viewportPanel.add(separator, spacerC);
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        for (UpdatableJPanel m : panels) {
            m.update();
        }
    }
}
