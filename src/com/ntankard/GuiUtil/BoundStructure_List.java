package com.ntankard.GuiUtil;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class BoundStructure_List<T extends BoundStructure_Generator> extends JPanel implements Updatable {

    /**
     * GUI Objects
     */
    private JList structure_list;
        private Button_PanelGenerator buttons;

    /**
     * The master content of the list
     */
    private ArrayList<T> objects;

    /**
     * The names of the above objects
     */
    private DefaultListModel model;

    /**
     * Reference object used to for new objects
     */
    private T newObject;

    /**
     * The function called when execute is pressed
     */
    private String executeFunction;

    /**
     * The top level GUI
     */
    private Updatable master;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Create a new List designed to edit objects
     * @param objects
     * @param master
     * @param newObject
     * @param <T>
     * @return
     */
    public static <T extends BoundStructure_Generator> BoundStructure_List newStandardEditList(ArrayList<T> objects,
                                                                                               Updatable master,
                                                                                               T newObject){
        BoundStructure_List list = new BoundStructure_List(objects,master);
        list.addNewBtn(newObject);
        list.addEditBtn();
        list.addDeleteBtn();
        return list;
    }

    /**
     * Create a new List designed to execute objects
     * @param objects
     * @param master
     * @param executeFunction
     * @param <T>
     * @return
     */
    public static <T extends BoundStructure_Generator> BoundStructure_List newStandardExecuteList(ArrayList<T> objects,
                                                                                                  Updatable master,
                                                                                                  String executeFunction){
        BoundStructure_List list = new BoundStructure_List(objects,master);
        list.addExecuteBtn(executeFunction,executeFunction);
        return list;
    }


    /**
     *
     * @param objects
     */
    public BoundStructure_List(ArrayList<T> objects, Updatable master) {
        this.objects = objects;
        this.master = master;
        this.buttons = new Button_PanelGenerator();
        this.model = new DefaultListModel();
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();

        structure_list = new JList(model);

        this.setBorder(new EmptyBorder(12, 12, 12, 12));
        this.setLayout(new BorderLayout());
        this.add(structure_list, BorderLayout.CENTER);
        this.add(buttons.getButtonPanel(), BorderLayout.SOUTH);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Buttons #########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Add a New button to the list
     * @param newObject
     */
    public void addNewBtn(T newObject){
        this.newObject = newObject;
        JButton new_btn = new JButton("New");
        new_btn.addActionListener(e -> onNew());
        buttons.addButton(new_btn);
        createUIComponents();
    }

    /**
     * Add an Edit button to the list
     */
    public void addEditBtn(){
        JButton edit_btn = new JButton("Edit");
        edit_btn.addActionListener(e -> onEdit());
        buttons.addButton(edit_btn);
        createUIComponents();
    }

    /**
     * Add a Delete button to the list
     */
    public void addDeleteBtn(){
        JButton delete_btn = new JButton("Delete");
        delete_btn.addActionListener(e -> onDelete());
        buttons.addButton(delete_btn);
        createUIComponents();
    }

    /**
     * Add a Execute button to the list
     * @param btnText
     * @param executeFunction
     */
    public void addExecuteBtn(String btnText, String executeFunction){
        this.executeFunction = executeFunction;
        JButton execute_btn = new JButton(btnText);
        execute_btn.addActionListener(e -> onExecute());
        buttons.addButton(execute_btn);
        createUIComponents();
    }

    /**
     * Create a new structure
     */
    public void onNew(){
        T n = (T)newObject.clone();
        if(BoundStructure_Dialog.openPanel(n.getPanel()))
        {
            objects.add(n);
        }
        update();
        if(master != null) {
            master.notifyUpdate();
        }
    }

    /**
     * Edit an existing object
     */
    public void onEdit(){
        if(structure_list.getSelectedIndex() != -1)
        {
            BoundStructure_Dialog.openPanel(objects.get(structure_list.getSelectedIndex()).getPanel());
            update();
        }
        if(master != null) {
            master.notifyUpdate();
        }
    }

    /**
     * Delete an object
     */
    public void onDelete(){
        if(structure_list.getSelectedIndex() != -1)
        {
            objects.remove(structure_list.getSelectedIndex());
            update();
        }
        if(master != null) {
            master.notifyUpdate();
        }
    }

    /**
     * Execute an object
     */
    public void onExecute(){
        if(structure_list.getSelectedIndex() != -1) {
            T toExecute = objects.get(structure_list.getSelectedIndex());
            if(BoundStructure_Dialog.openPanel(toExecute.getPanel())){
                update();
                try {
                    toExecute.getClass().getMethod(executeFunction).invoke(toExecute);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        if(master != null) {
            master.notifyUpdate();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     * Bottom of the tree
     */
    public void update()
    {
        model.clear();

        if(objects != null) {
            for (BoundStructure_Generator o : objects) {
                model.addElement(o.toString());
            }
        }

        // force update
        structure_list.setModel(model);
    }

    /**
     * @inheritDoc
     * Bottom of the tree
     */
    public void notifyUpdate(){}

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
        private final GridConstraints button_C =  new GridConstraints(
                0, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null);

        /**
         *
         * @param toAdd
         */
        public void addButton(JButton toAdd){
            buttons.add(toAdd);
        }

        /**
         * Finalize the panel by adding all the buttons
         * @return
         */
        public JPanel getButtonPanel(){
            if(buttons.size() == 0){
                return new JPanel();
            }

            JPanel button_panel = new JPanel(new GridLayoutManager(1, buttons.size(), new Insets(0, 0, 0, 0), -1, -1));

            for(int i=0;i<buttons.size();i++){
                button_C.setColumn(i);
                button_panel.add(buttons.get(i),button_C);
            }

            return button_panel;
        }
    }
}