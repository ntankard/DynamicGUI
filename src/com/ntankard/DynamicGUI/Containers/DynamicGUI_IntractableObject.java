package com.ntankard.DynamicGUI.Containers;

import com.ntankard.CoreObject.CoreObject;
import com.ntankard.DynamicGUI.Components.Object.DynamicGUI_IntractableObject_Impl;
import com.ntankard.DynamicGUI.Util.Containers.ControllablePanel;
import com.ntankard.DynamicGUI.Util.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import javax.swing.*;

public class DynamicGUI_IntractableObject<T extends CoreObject> extends ControllablePanel<DynamicGUI_IntractableObject_Impl, ControllablePanel> {

    /**
     * Open a dialog containing a new DynamicGUI_IntractableObject with controls
     *
     * @param corePanel The main panel to display
     * @param <T>       The type of the base instance
     * @return True if the object was modified
     */
    public static <T extends CoreObject> boolean openIntractableObjectDialog(DynamicGUI_IntractableObject<T> corePanel) {
        final boolean[] change = {false};
        JDialog dialog = new JDialog();
        corePanel.saveButton(new FinalizeNotifier() {
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
        });


        dialog.setContentPane(corePanel);
        dialog.setModal(true); // block until complete
        dialog.getRootPane().setDefaultButton(corePanel.getButtonPanel().getMainBtn());
        dialog.pack();
        dialog.setVisible(true);
        return change[0];
    }

    /**
     * Constructor
     *
     * @param baseInstance The instance to interact with
     * @param master       The parent of this object to be notified if data changes
     */
    public DynamicGUI_IntractableObject(T baseInstance, Updatable master) {
        this(baseInstance, true, master);
    }

    /**
     * Constructor
     *
     * @param baseInstance The instance to interact with
     * @param vertical     Should the display be layered out vertically? Otherwise horizontally
     * @param master       The parent of this object to be notified if data changes
     */
    public DynamicGUI_IntractableObject(T baseInstance, boolean vertical, Updatable master) {
        super(master);
        setMainPanel(new DynamicGUI_IntractableObject_Impl<>(baseInstance, vertical, master));
        update();
    }

    /**
     * Add save and cancel buttons
     *
     * @param notifier Callback for when the buttons are pressed
     * @return This
     */
    public DynamicGUI_IntractableObject<T> saveButton(FinalizeNotifier notifier) {
        getMainPanel().setSaveOnUpdate(false);
        addControlButtons(notifier);
        update();
        return this;
    }

    /**
     * Set what level of verbosity should be shown?
     *
     * @param verbosity What level of verbosity should be shown?
     * @return This
     */
    public DynamicGUI_IntractableObject<T> setVerbosity(int verbosity) {
        getMainPanel().setVerbosity(verbosity);
        update();
        return this;
    }

    /**
     * Set a user set source for the locale, numberFormat used if not set
     *
     * @param localeSource A user set source for the locale, numberFormat used if not set
     * @return This
     */
    public DynamicGUI_IntractableObject<T> setLocaleSource(CurrencyDecoder_NumberFormatSource localeSource) {
        getMainPanel().setLocaleSource(localeSource);
        update();
        return this;
    }

    /**
     * Add a button panel with the save and cancel button
     */
    private void addControlButtons(FinalizeNotifier notifier) {
        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            getMainPanel().execute();
            if (notifier != null) {
                notifier.complete();
            }
        });
        addButton(saveBtn, true);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> {
            getMainPanel().discard();
            if (notifier != null) {
                notifier.cancel();
            }
        });
        addButton(cancelBtn);
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
