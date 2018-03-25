package com.ntankard.DynamicGUI.Components.List.Controls;



import javax.swing.*;

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