package com.ntankard.DynamicGUI.Components.List.Display;

import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.util.ArrayList;

public abstract class BoundArray extends JScrollPane implements Updatable {

    /**
     * The master content of the list
     */
    protected ArrayList objects;

    /**
     * The top level GUI
     */
    protected Updatable master;

    /**
     * @param objects
     * @param master
     */
    public BoundArray(ArrayList objects, Updatable master) {
        super();
        this.objects = objects;
        this.master = master;
    }

    @Override
    public void notifyUpdate() {
    }

    public abstract Object getSelectedItem();
}
