package com.ntankard.DynamicGUI.Components.List.Display;

import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.util.List;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class BoundArray_JList extends BoundArray {

    /**
     * GUI Objects
     */
    private JList<String> structure_list;

    /**
     * The names of the above objects
     */
    private DefaultListModel<String> model;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param objects
     */
    public BoundArray_JList(List objects, Updatable master) {
        super(objects, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.model = new DefaultListModel<>();
        this.structure_list = new JList<>(model);
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

        if (getObjects() != null) {
            for (Object o : getObjects()) {
                model.addElement(o.toString());
            }
        }

        // force update
        structure_list.setModel(model);
    }

    /**
     * @inheritDoc
     */
    @Override
    protected ListSelectionModel getListSelectionModel() {
        return structure_list.getSelectionModel();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected Object getItemFromSelectIndex(int i) {
        return getObjects().get(i);
    }
}