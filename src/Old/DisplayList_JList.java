//package com.ntankard.DynamicGUI.Components.List.Types;
//
//import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList_Impl;
//import com.ntankard.DynamicGUI.Util.Updatable;
//
//import javax.swing.*;
//import java.util.List;
//
///**
// * Created by Nicholas on 26/06/2016.
// */
//public class DisplayList_JList<T> extends DynamicGUI_DisplayList_Impl<T> {
//
//    /**
//     * GUI Objects
//     */
//    private JList<String> structure_list;
//
//    /**
//     * The names of the above objects
//     */
//    private DefaultListModel<String> model;
//
//    //------------------------------------------------------------------------------------------------------------------
//    //############################################## Constructors ######################################################
//    //------------------------------------------------------------------------------------------------------------------
//
//    /**
//     * Constructor
//     *
//     * @param objects The list of objects to display
//     * @param master  The parent of this object to be notified if data changes
//     */
//    public DisplayList_JList(List<T> objects, Updatable master) {
//        super(objects, master);
//        createUIComponents();
//        update();
//    }
//
//    /**
//     * Create the GUI components
//     */
//    private void createUIComponents() {
//        this.model = new DefaultListModel<>();
//        this.structure_list = new JList<>(model);
//        this.setViewportView(structure_list);
//    }
//
//    //------------------------------------------------------------------------------------------------------------------
//    //############################################# Extended methods ###################################################
//    //------------------------------------------------------------------------------------------------------------------
//
//    /**
//     * @inheritDoc Bottom of the tree
//     */
//    @Override
//    public void update() {
//        model.clear();
//
//        if (getObjects() != null) {
//            for (T o : getObjects()) {
//                model.addElement(o.toString());
//            }
//        }
//
//        // force update
//        structure_list.setModel(model);
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    public ListSelectionModel getListSelectionModel() {
//        return structure_list.getSelectionModel();
//    }
//
//    /**
//     * @inheritDoc
//     */
//    @Override
//    protected T getItemFromSelectIndex(int i) {
//        return getObjects().get(i);
//    }
//}
