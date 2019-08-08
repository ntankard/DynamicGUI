package com.ntankard.DynamicGUI.Components.Object;

import com.ntankard.DynamicGUI.Util.FrameWrapper;
import com.ntankard.DynamicGUI.Util.Swing.Containers.ControllablePanel;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;

public class DynamicGUI_IntractableObject extends ControllablePanel<DynamicGUI_IntractableObject_Impl, ControllablePanel> {

    //------------------------------------------------------------------------------------------------------------------
    //############################################ Master Factories ####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Create a new DynamicGUI_IntractableObject_Impl with controls if needed
     *
     * @param baseInstance The instance to interact with
     * @param verbosity    What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param saveOnUpdate Should changes occurs straight away or after save is pressed?
     * @param master       The top level GUI
     * @param sources      Sources of data that can be set for various objects
     * @param <T>          The type of the base instance
     * @return A ControllablePanel containing a DynamicGUI_IntractableObject_Impl
     */
    public static <T> DynamicGUI_IntractableObject newIntractableObjectPanel(T baseInstance, int verbosity, boolean saveOnUpdate, Updatable master, Object... sources) {
        DynamicGUI_IntractableObject container = new DynamicGUI_IntractableObject(master);

        DynamicGUI_IntractableObject_Impl main = new DynamicGUI_IntractableObject_Impl<>(baseInstance, verbosity, saveOnUpdate, master, sources);
        container.setMainPanel(main);

        if (!saveOnUpdate) {
            container.addControlButtons();
        }

        return container;
    }

    /**
     * Open a frame containing a new DynamicGUI_IntractableObject_Impl with controls
     *
     * @param baseInstance The instance to interact with
     * @param verbosity    What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param sources      Sources of data that can be set for various objects
     * @param master       The top level GUI
     * @param <T>          The type of the base instance
     */
    public static <T> void openIntractableObjectFrame(T baseInstance, int verbosity, Updatable master, Object... sources) {
        FrameWrapper.open(baseInstance.getClass().getSimpleName(), newIntractableObjectPanel(baseInstance, verbosity, false, master, sources));
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Core Object ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    private DynamicGUI_IntractableObject(Updatable master) {
        super(master);
    }

    /**
     * Add a button panel with the save and cancel button
     */
    private void addControlButtons() {
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> getMainPanel().execute());
        addButton(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> getMainPanel().discard());
        addButton(cancelBtn);
    }
}
