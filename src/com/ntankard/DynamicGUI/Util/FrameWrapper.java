package com.ntankard.DynamicGUI.Util;

import com.ntankard.DynamicGUI.Util.Swing.Base.UpdatableJPanel;

import javax.swing.*;
import java.awt.*;

public class FrameWrapper {

    /**
     * Open a panel as a frame
     *
     * @param title The title of the frame
     * @param panel The panel to render
     */
    public static void open(String title, UpdatableJPanel panel) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(title);
            frame.setContentPane(panel);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
            frame.pack();
            frame.setVisible(true);

            frame.repaint();
        });
    }
}
