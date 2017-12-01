package com.ntankard.DynamicGUI.Components.List;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.ntankard.DynamicGUI.Components.EditablePanel.BoundComposite_JPanelGenerator;
import com.ntankard.DynamicGUI.Components.EditablePanel.BoundStructure_Dialog;
import com.ntankard.DynamicGUI.Components.List.Display.BoundArray;
import com.ntankard.DynamicGUI.Components.List.Display.BoundArray_JList;
import com.ntankard.DynamicGUI.Components.List.Display.BoundArray_JTable;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class BoundArray_Interface extends JPanel implements Updatable {

    /**
     * GUI Objects
     */
    private BoundArray display;
    private Button_PanelGenerator buttons;

    /**
     * The top level GUI
     */
    private Updatable master;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    public static BoundArray_Interface newStandardDisplayList(ArrayList objects,
                                                              Updatable master) {
        BoundArray_Interface list = new BoundArray_Interface(master, new BoundArray_JList(objects, master));
        list.addEditBtn();
        return list;
    }

    public static BoundArray_Interface newStandardDisplayTable(ArrayList objects,
                                                               Updatable master) {
        BoundArray_Interface list = new BoundArray_Interface(master, new BoundArray_JTable(objects, master,10));
        list.addEditBtn();
        return list;
    }

    public static BoundArray_Interface newStandardDisplayTable(ArrayList objects,
                                                               Updatable master,
                                                               int verbosity) {
        BoundArray_Interface list = new BoundArray_Interface(master, new BoundArray_JTable(objects, master,verbosity));
        list.addEditBtn();
        return list;
    }

    public BoundArray_Interface(Updatable master, BoundArray display) {
        this.master = master;
        this.buttons = new Button_PanelGenerator();
        this.display = display;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();

        this.setBorder(new EmptyBorder(12, 12, 12, 12));
        this.setLayout(new BorderLayout());
        this.add(display, BorderLayout.CENTER);
        this.add(buttons.getButtonPanel(), BorderLayout.SOUTH);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Buttons #########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Add an Edit button to the list
     */
    public void addEditBtn() {
        JButton edit_btn = new JButton("Edit");
        edit_btn.addActionListener(e -> onEdit());
        buttons.addButton(edit_btn);
        createUIComponents();
    }

    /**
     * Edit an existing object
     */
    public void onEdit() {
        Object selected = display.getSelectedItem();
        if (selected != null) {
            BoundStructure_Dialog.openPanel(BoundComposite_JPanelGenerator.generate(selected));
            update();
        }
        if (master != null) {
            master.notifyUpdate();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
        display.update();
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    public void notifyUpdate() {
    }

    /**
     * Formatter for a variable amount of buttons
     */
    private class Button_PanelGenerator {

        /**
         * All the buttons to add
         */
        private ArrayList<JButton> buttons = new ArrayList<>();

        /**
         * The constraint for a a button
         */
        private final GridConstraints button_C = new GridConstraints(
                0, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null);

        /**
         * @param toAdd
         */
        public void addButton(JButton toAdd) {
            buttons.add(toAdd);
        }

        /**
         * Finalize the panel by adding all the buttons
         *
         * @return
         */
        public JPanel getButtonPanel() {
            if (buttons.size() == 0) {
                return new JPanel();
            }

            JPanel button_panel = new JPanel(new GridLayoutManager(1, buttons.size(), new Insets(0, 0, 0, 0), -1, -1));

            for (int i = 0; i < buttons.size(); i++) {
                button_C.setColumn(i);
                button_panel.add(buttons.get(i), button_C);
            }

            return button_panel;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Unused ########################################################
    //------------------------------------------------------------------------------------------------------------------
//
//    /**
//     * Add a New button to the list
//     *
//     * @param newObject
//     */
//    public void addNewBtn(Object newObject) {
//        this.newObject = newObject;
//        JButton new_btn = new JButton("New");
//        new_btn.addActionListener(e -> onNew());
//        buttons.addButton(new_btn);
//        createUIComponents();
//    }
//
//    /**
//     * Add a Delete button to the list
//     */
//    public void addDeleteBtn() {
//        JButton delete_btn = new JButton("Delete");
//        delete_btn.addActionListener(e -> onDelete());
//        buttons.addButton(delete_btn);
//        createUIComponents();
//    }
//
//    /**
//     * Add a Execute button to the list
//     *
//     * @param btnText
//     * @param executeFunction
//     */
//    public void addExecuteBtn(String btnText, String executeFunction) {
//        this.executeFunction = executeFunction;
//        JButton execute_btn = new JButton(btnText);
//        execute_btn.addActionListener(e -> onExecute());
//        buttons.addButton(execute_btn);
//        createUIComponents();
//    }
//
//    /**
//     * Create a new structure
//     */
//    public void onNew() {
//        /*T n = (T)newObject.clone();
//        // @TODO finish this
//        if(BoundStructure_Dialog.openPanel(n.getPanel()))
//        {
//            objects.add(n);
//        }
//        update();
//        if(master != null) {
//            master.notifyUpdate();
//        }*/
//    }
//
//    /**
//     * Delete an object
//     */
//    public void onDelete() {
//        /*if (structure_list.getSelectedIndex() != -1) {
//            objects.remove(structure_list.getSelectedIndex());
//            update();
//        }
//        if (master != null) {
//            master.notifyUpdate();
//        }*/
//    }
//
//    /**
//     * Execute an object
//     */
//    public void onExecute() {
//        //@TODO finish this
//
//        /*if(structure_list.getSelectedIndex() != -1) {
//            T toExecute = objects.get(structure_list.getSelectedIndex());
//            if(BoundStructure_Dialog.openPanel(toExecute.getPanel())){
//                update();
//                try {
//                    toExecute.getClass().getMethod(executeFunction).invoke(toExecute);
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        if(master != null) {
//            master.notifyUpdate();
//        }*/
//    }
}