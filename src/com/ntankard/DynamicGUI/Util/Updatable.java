package com.ntankard.DynamicGUI.Util;

import javax.swing.*;

/**
 * Created by Nicholas on 24/05/2016.
 */
public interface Updatable {

    /**
     * Called whe the model is changes, should be forwarded to children (must not trigger notifyUpdate)
     */
    void update();

    /**
     * To be called when a model update occurs, should be forwarded to parent, only highest parent should trigger update
     */
    void notifyUpdate();

    abstract class UpdatableJPanel extends JPanel implements Updatable{
        private Updatable master;
        protected UpdatableJPanel(Updatable master){
            this.master = master;
        }
        @Override
        public void notifyUpdate() {
            master.notifyUpdate();
        }
    }
    abstract class UpdatableJScrollPane extends JScrollPane implements Updatable{
        private Updatable master;
        protected UpdatableJScrollPane(Updatable master){
            this.master = master;
        }
        @Override
        public void notifyUpdate() {
            master.notifyUpdate();
        }}
}
