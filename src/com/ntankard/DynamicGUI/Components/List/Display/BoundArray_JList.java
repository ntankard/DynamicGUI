package com.ntankard.DynamicGUI.Components.List.Display;

import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class BoundArray_JList extends BoundArray {

    /**
     * GUI Objects
     */
    private JList structure_list;

    /**
     * The names of the above objects
     */
    private DefaultListModel model;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param objects
     */
    public BoundArray_JList(ArrayList objects, Updatable master) {
        super(objects, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.model = new DefaultListModel();
        this.structure_list = new JList(model);
        this.setViewportView(structure_list);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
        model.clear();

        if (objects != null) {
            for (Object o : objects) {
                model.addElement(o.toString());
            }
        }

        // force update
        structure_list.setModel(model);
    }

    public Object getSelectedItem() {
        if (structure_list.getSelectedIndex() != -1) {
            return objects.get(structure_list.getSelectedIndex());
        }
        return null;
    }
}