package com.ntankard.DynamicGUI.Composite;

import com.ntankard.DynamicGUI.Components.Base.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.ButtonPanel;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class DynamicGUI_EditableList extends JPanel implements Updatable {

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Factories #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public static <T> DynamicGUI_EditableList newStandardDisplayList(List<T> objects, Updatable master) {
        return new DynamicGUI_EditableList(master, DynamicGUI_DisplayList.newStandardDisplayList(objects, master));
    }

    public static <T> DynamicGUI_EditableList newStandardDisplayTable(List<T> objects, Updatable master) {
        return newStandardDisplayTable(objects, TRACE_DISPLAY,master);
    }

    public static <T> DynamicGUI_EditableList newStandardDisplayTable(List<T> objects, int verbosity,Updatable master) {
        return new DynamicGUI_EditableList(master, DynamicGUI_DisplayList.newStandardDisplayTable(objects, verbosity,master));
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Core ##########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * GUI Objects
     */
    private DynamicGUI_DisplayList display;
    private ButtonPanel buttons;

    /**
     * The top level GUI
     */
    private Updatable master;

    /**
     * Default constructor
     *
     * @param master
     * @param display
     */
    private DynamicGUI_EditableList(Updatable master, DynamicGUI_DisplayList display) {
        this.master = master;
        this.buttons = new ButtonPanel();
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
        this.add(buttons, BorderLayout.SOUTH);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Buttons #########################################################
    //------------------------------------------------------------------------------------------------------------------

    public void addButton(ListControl_Button button) {
        buttons.addButton(button);
        button.setControllableList(display);
        createUIComponents();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void update() {
        display.update();
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void notifyUpdate() {
        master.notifyUpdate();
    }


    public static class ListControl_Button extends JButton {

        /**
         * The list containing this button
         */
        private DynamicGUI_DisplayList coreList;

        /**
         * Default constructor
         *
         * @param name The name to put on the button
         */
        public ListControl_Button(String name) {
            super(name);
        }

        /**
         * Called when the button is added to a DynamicGUI_DisplayList to register the parent
         *
         * @param coreList The list to link to
         */
        public void setControllableList(DynamicGUI_DisplayList coreList) {
            this.coreList = coreList;
        }

        /**
         * Accessor to allow the button (and its action action) to get access to the list it is to act on
         *
         * @return The list containing this button
         */
        public DynamicGUI_DisplayList getCoreList() {
            return coreList;
        }
    }
}


//public class Edit_ListControl_Button extends ListControl_Button {

//    /**
//     * Add an Edit button to the list
//     */
//    public void addEditBtn() {
//        JButton edit_btn = new JButton("Edit");
//        edit_btn.addActionListener(e -> onEdit());
//        buttons.addButton(edit_btn);
//        createUIComponents();
//    }
//
//    /**
//     * Edit an existing object
//     */
//    public void onEdit() {
//        Object selected = display.getSelectedItem();
//        if (selected != null) {
//            BoundStructure_Dialog.openPanel(BoundComposite_JPanelGenerator.generate(selected));
//            update();
//        }
//        if (master != null) {
//            master.notifyUpdate();
//        }
//    }
//}



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