package com.ntankard.DynamicGUI.GuiUtil;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class BoundStructure_Table<T extends BoundStructure_Generator> extends JPanel implements Updatable {

    /**
     * GUI Objects
     */
    private JTable structure_table;
    private Button_PanelGenerator buttons;

    /**
     * The master content of the list
     */
    private ArrayList<T> objects;

    /**
     * The names of the above objects
     */
    private DefaultTableModel model;

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

    public static <T extends BoundStructure_Generator> BoundStructure_Table newStandardEditTable(ArrayList<T> objects,
                                                                                                 Updatable master,
                                                                                                 T newObject) {
        BoundStructure_Table list = new BoundStructure_Table(objects, master);
        list.addNewBtn(newObject);
        list.addEditBtn();
        list.addDeleteBtn();
        return list;
    }


    /**
     * @param objects
     */
    public BoundStructure_Table(ArrayList<T> objects, Updatable master) {
        this.objects = objects;
        this.master = master;
        this.buttons = new Button_PanelGenerator();
        this.model = new DefaultTableModel();
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();

        JScrollPane scrollPane = new JScrollPane();
        structure_table = new JTable(model);
        scrollPane.setViewportView(structure_table);


        String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};

        Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", new Integer(5), new Boolean(false)},
                {"John", "Doe",
                        "Rowing", new Integer(3), new Boolean(true)},
                {"Sue", "Black",
                        "Knitting", new Integer(2), new Boolean(false)},
                {"Jane", "White",
                        "Speed reading", new Integer(20), new Boolean(true)},
                {"Joe", "Brown",
                        "Pool", new Integer(10), new Boolean(false)}
        };


        // JTable test =  new JTable(data, columnNames);
        // scrollPane.setViewportView(test);
        this.setBorder(new EmptyBorder(12, 12, 12, 12));
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttons.getButtonPanel(), BorderLayout.SOUTH);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Buttons #########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Add a New button to the list
     *
     * @param newObject
     */
    public void addNewBtn(T newObject) {
        this.newObject = newObject;
        JButton new_btn = new JButton("New");
        new_btn.addActionListener(e -> onNew());
        buttons.addButton(new_btn);
        createUIComponents();
    }

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
     * Add a Delete button to the list
     */
    public void addDeleteBtn() {
        JButton delete_btn = new JButton("Delete");
        delete_btn.addActionListener(e -> onDelete());
        buttons.addButton(delete_btn);
        createUIComponents();
    }

    /**
     * Add a Execute button to the list
     *
     * @param btnText
     * @param executeFunction
     */
    public void addExecuteBtn(String btnText, String executeFunction) {
        this.executeFunction = executeFunction;
        JButton execute_btn = new JButton(btnText);
        execute_btn.addActionListener(e -> onExecute());
        buttons.addButton(execute_btn);
        createUIComponents();
    }

    /**
     * Create a new structure
     */
    public void onNew() {
       /* T n = (T)newObject.clone();
        if(BoundStructure_Dialog.openPanel(n.getPanel()))
        {
            objects.add(n);
        }
        update();
        if(master != null) {
            master.notifyUpdate();
        }*/
    }

    /**
     * Edit an existing object
     */
    public void onEdit() {
//        if(structure_list.getSelectedIndex() != -1)
//        {
//            //BoundStructure_Generator o = objects.get(structure_list.getSelectedIndex());
//
//            // ReflectionGuiGenerator ref = new ReflectionGuiGenerator(objects.get(structure_list.getSelectedIndex()));
//
//            ReflectionGuiGenerator r = new ReflectionGuiGenerator();
//            BoundStructure_Dialog.openPanel(ReflectionGuiGenerator.generate(objects.get(structure_list.getSelectedIndex())));
//
//            update();
//        }
//        if(master != null) {
//            master.notifyUpdate();
//        }
    }

    /**
     * Delete an object
     */
    public void onDelete() {
//        if(structure_list.getSelectedIndex() != -1)
//        {
//            objects.remove(structure_list.getSelectedIndex());
//            update();
//        }
//        if(master != null) {
//            master.notifyUpdate();
//        }
    }

    /**
     * Execute an object
     */
    public void onExecute() {
//        if(structure_list.getSelectedIndex() != -1) {
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
//        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
        model = new DefaultTableModel();

        for (String col : objects.get(0).getHeaders()) {
            model.addColumn(col);
        }

        // model.addColumn("test1");
        // model.addColumn("test2");

        String[] dat = {"a", "b"};

        if (objects != null) {
            for (BoundStructure_Generator o : objects) {
                model.addRow(o.getStringParts());
            }
        }

        // force update
        structure_table.setModel(model);
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
}