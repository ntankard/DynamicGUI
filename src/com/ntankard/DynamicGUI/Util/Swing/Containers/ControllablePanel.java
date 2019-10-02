package com.ntankard.DynamicGUI.Util.Swing.Containers;

import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;
import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJScrollPane;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A panel consisting of a main viewing area, a button panel at the bottom and a controller panel on the side
 * Types are passed to ease the process of passing data between the panels inb children
 *
 * @param <S> The type of panel in the center (the main panel)
 * @param <C> The type on panel on the side (the controller)
 */
public class ControllablePanel<S extends UpdatableJScrollPane, C extends ControllablePanel> extends UpdatableJPanel {

    /**
     * The main panel displayed (required)
     */
    private S mainPanel = null;

    /**
     * The controller panel (optional)
     */
    private C controlPanel = null;

    /**
     * The button panel at the bottom of the main panel (can be empty)
     */
    private ButtonPanel buttonPanel;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    protected ControllablePanel(Updatable master) {
        super(master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.setLayout(new BorderLayout());

        this.buttonPanel = new ButtonPanel();
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Add a button to the panel at the bottom
     *
     * @param toAdd The button to add
     */
    public void addButton(JButton toAdd) {
        buttonPanel.addButton(toAdd);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        if (mainPanel != null) {
            mainPanel.update();
        }
        if (controlPanel != null) {
            controlPanel.update();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Accessors #######################################################
    //------------------------------------------------------------------------------------------------------------------


    public S getMainPanel() {
        return mainPanel;
    }

    public C getControlPanel() {
        return controlPanel;
    }

    protected void setMainPanel(S mainPanel) {
        this.mainPanel = mainPanel;
        this.add(mainPanel, BorderLayout.CENTER);
    }

    protected void setControlPanel(C controlPanel) {
        this.controlPanel = controlPanel;
        this.add(controlPanel, BorderLayout.EAST);
    }

    public void setNorth(Component north) {
        this.add(north, BorderLayout.NORTH);
    }
}
