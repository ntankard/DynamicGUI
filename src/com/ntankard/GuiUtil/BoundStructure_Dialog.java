package com.ntankard.GuiUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.ntankard.DynamicGUI.PanelManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by Nicholas on 8/05/2016.
 */
public class BoundStructure_Dialog extends JDialog  {

    /**
     * Change if cant be loaded from old version
     */
    private static final long serialVersionUID = 2294104019148152866L;

    /**
     * GUI objects
     */
    private JPanel dialogPanel;
    private PanelManager structurePanel;
    private JPanel buttonPanel;
        private JButton buttonOK;
        private JButton buttonCancel;

    /**
     * Was the object changed
     */
    private boolean change;

    /**
     *
     * @param p
     * @return
     */
    public static boolean openPanel(PanelManager p) {
        BoundStructure_Dialog dialog = new BoundStructure_Dialog(p);
        dialog.pack();
        dialog.setVisible(true);
        return dialog.didChange();
    }

    /**
     * Was the OK button pressed
     * @return
     */
    public boolean didChange() {
        return change;
    }

    /**
     * Default constructor
     * @param structure
     */
    public BoundStructure_Dialog(PanelManager structure) {
        // load the child panel
        this.structurePanel = structure;

        // setup the GUI
        createUIComponents();

        // setup the dialog
        setModal(true); // block until complete
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        (structurePanel).registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        buttonOK = new JButton();
        buttonOK.setText("OK");
        buttonOK.addActionListener(e -> onOK());
        GridBagConstraints buttonOK_C = new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0);

        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        buttonCancel.addActionListener(e -> onCancel());
        GridBagConstraints buttonCancel_C = new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0);

        buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(12, 0, 0, 0));
        GridBagLayout GBL = new GridBagLayout();
        GBL.columnWidths = new int[] {0, 85, 80};
        GBL.columnWeights = new double[] {1.0, 0.0, 0.0};
        buttonPanel.setLayout(GBL);
        buttonPanel.add(buttonOK, buttonOK_C);
        buttonPanel.add(buttonCancel, buttonCancel_C);

        dialogPanel = new JPanel();
        dialogPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        dialogPanel.setLayout(new BorderLayout());
        dialogPanel.add(structurePanel, BorderLayout.CENTER);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(dialogPanel, BorderLayout.CENTER);
    }

    /**
     * Load the panels content into the object
     */
    private void onOK() {
        if(!structurePanel.validateState()){
            JOptionPane.showMessageDialog(this, "Invalid");
            return;
        }

        structurePanel.save();
        change = true;
        dispose();
    }

    /**
     * Don't save any changes
     */
    private void onCancel() {
        change = false;
        dispose();
    }
}