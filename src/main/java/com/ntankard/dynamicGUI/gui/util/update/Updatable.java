package com.ntankard.dynamicGUI.gui.util.update;

/**
 * Created by Nicholas on 24/05/2016.
 */
public interface Updatable {

    /**
     * Called when the model is changes, should be forwarded to children (must not trigger notifyUpdate)
     */
    void update();

    /**
     * To be called when a model update occurs, should be forwarded to parent, only highest parent should trigger update
     */
    void notifyUpdate();
}
