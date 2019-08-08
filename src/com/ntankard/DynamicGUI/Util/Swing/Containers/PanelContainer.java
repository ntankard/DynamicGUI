package com.ntankard.DynamicGUI.Util.Swing.Containers;

import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJScrollPane;
import com.ntankard.DynamicGUI.Util.Updatable;

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
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    protected PanelContainer(Updatable master) {
        super(master);
    }

    /**
     * Create the GUI components
     */
    protected void createUIComponents() {
        viewportPanel = new JPanel(new GridBagLayout());

        componentC = new GridBagConstraints();
        componentC.weightx = 1;
        componentC.fill = GridBagConstraints.BOTH;
        componentC.gridy = 0;

        GridBagConstraints spacerC = new GridBagConstraints();
        spacerC.weighty = 1;
        spacerC.gridy = 0;
        viewportPanel.add(separator, spacerC);

        this.setViewportView(viewportPanel);
    }

    /**
     * Add a MemberPanel to the panel
     *
     * @param memberPanel The panel to add
     */
    protected void addMember(UpdatableJPanel memberPanel) {
        viewportPanel.remove(separator);

        componentC.gridy++;
        viewportPanel.add(memberPanel, componentC);
        panels.add(memberPanel);

        GridBagConstraints spacerC = new GridBagConstraints();
        spacerC.weighty = 1;
        spacerC.gridy = componentC.gridy + 1;
        viewportPanel.add(separator, spacerC);
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
