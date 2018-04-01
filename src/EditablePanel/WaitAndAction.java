package com.ntankard.DynamicGUI.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class WaitAndAction extends AbstractAction {

    private SwingWorker swingWorker;

    public WaitAndAction(String name, SwingWorker action) {
        super(name);
        this.swingWorker = action;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {

        Window win = SwingUtilities.getWindowAncestor((AbstractButton) evt.getSource());
        final JDialog dialog = new JDialog(win, "Loading", Dialog.ModalityType.APPLICATION_MODAL);

        swingWorker.addPropertyChangeListener(evt1 -> {
            if (evt1.getPropertyName().equals("state")) {
                if (evt1.getNewValue() == SwingWorker.StateValue.DONE) {
                    dialog.dispose();
                }
            }
        });
        swingWorker.execute();

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(new JLabel("Please wait......."), BorderLayout.PAGE_START);
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(win);
        dialog.setVisible(true);
    }
}
