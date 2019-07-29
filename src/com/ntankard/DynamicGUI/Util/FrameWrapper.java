package com.ntankard.DynamicGUI.Util;

import javax.swing.JFrame;

public class FrameWrapper {

    /**
     * Open a panel as a frame
     *
     * @param title The title of the frame
     * @param panel The panel to render
     */
    public static void open(String title, Updatable.UpdatableJPanel panel) {
        JFrame frame = new JFrame(title);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);

        frame.repaint();
    }
}