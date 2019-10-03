package com.ntankard.DynamicGUI.Components.Object;

import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.CurrencyDecoder_LocaleSource;
import com.ntankard.DynamicGUI.Util.Swing.Containers.ControllablePanel;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;

public class DynamicGUI_IntractableObject extends ControllablePanel<DynamicGUI_IntractableObject_Impl, ControllablePanel> {

    //------------------------------------------------------------------------------------------------------------------
    //########################################## Overloaded Factories ##################################################
    //------------------------------------------------------------------------------------------------------------------

    public static <T> DynamicGUI_IntractableObject newIntractableObjectPanel(T baseInstance, int verbosity, boolean saveOnUpdate, Updatable master, Object... sources) {
        return newIntractableObjectPanel(baseInstance, verbosity, saveOnUpdate, null, master, sources);
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################## Container Factories ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Open a dialog containing a new DynamicGUI_IntractableObject_Impl with controls
     *
     * @param baseInstance The instance to interact with
     * @param verbosity    What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param master       The top level GUI
     * @param sources      Sources of data that can be set for various objects
     * @param <T>          The type of the base instance
     */
    public static <T> boolean openIntractableObjectDialog(T baseInstance, int verbosity, CurrencyDecoder_LocaleSource localeSource, Updatable master, Object... sources) {
        final boolean[] change = {false};
        JDialog dialog = new JDialog();
        DynamicGUI_IntractableObject panel = newIntractableObjectPanel(baseInstance, verbosity, false, new FinalizeNotifier() {
            @Override
            public void cancel() {
                change[0] = false;
                dialog.dispose();
            }

            @Override
            public void complete() {
                change[0] = true;
                dialog.dispose();
            }
        }, master, sources);
        panel.getMainPanel().setLocaleInspector(localeSource);
        dialog.setContentPane(panel);
        dialog.setModal(true); // block until complete
        dialog.getRootPane().setDefaultButton(panel.getDefaultButton());
        dialog.pack();
        dialog.setVisible(true);
        return change[0];
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################ Master Factories ####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Create a new DynamicGUI_IntractableObject_Impl with controls if needed
     *
     * @param baseInstance The instance to interact with
     * @param verbosity    What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param saveOnUpdate Should changes occurs straight away or after save is pressed?
     * @param notifier     The object top notify when the panel is finished
     * @param master       The top level GUI
     * @param sources      Sources of data that can be set for various objects
     * @param <T>          The type of the base instance
     * @return A ControllablePanel containing a DynamicGUI_IntractableObject_Impl
     */
    public static <T> DynamicGUI_IntractableObject newIntractableObjectPanel(T baseInstance, int verbosity, boolean saveOnUpdate, FinalizeNotifier notifier, Updatable master, Object... sources) {
        DynamicGUI_IntractableObject container = new DynamicGUI_IntractableObject(master);

        DynamicGUI_IntractableObject_Impl main = new DynamicGUI_IntractableObject_Impl<>(baseInstance, verbosity, saveOnUpdate, master, sources);
        container.setMainPanel(main);

        if (!saveOnUpdate) {
            container.addControlButtons(notifier);
        }

        return container;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Core Object ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * The save button, stored because it acts as the default button
     */
    private JButton saveBtn;

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
    private void addControlButtons(FinalizeNotifier notifier) {
        saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            getMainPanel().execute();
            if (notifier != null) {
                notifier.complete();
            }
        });
        addButton(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            getMainPanel().discard();
            if (notifier != null) {
                notifier.cancel();
            }
        });
        addButton(cancelBtn);
    }

    /**
     * Get the button to be called when the user hits enter on a dialog
     *
     * @return The button to be called when the user hits enter on a dialog
     */
    public JButton getDefaultButton() {
        return saveBtn;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################ Interface Objects ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Notify when the user is finished with the panel
     */
    public interface FinalizeNotifier {
        /**
         * The user has canceled the interaction
         */
        void cancel();

        /**
         * The user has completed the interaction
         */
        void complete();
    }
}
