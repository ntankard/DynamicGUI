package com.ntankard.DynamicGUI.Components.List.Display;

import com.ntankard.DynamicGUI.Generator.ObjectField;
import com.ntankard.DynamicGUI.Generator.ObjectReflector;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class BoundArray_JTable extends BoundArray {

    /**
     * GUI Objects
     */
    private JTable structure_table;

    /**
     * The names of the above objects
     */
    private DefaultTableModel model;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param objects
     */
    public BoundArray_JTable(ArrayList objects, Updatable master) {
        super(objects, master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.model = new DefaultTableModel();
        this.structure_table = new JTable(model);
        this.setViewportView(structure_table);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
        ArrayList<ObjectField> fields = ObjectReflector.getFields(objects.get(0));
        model = new DefaultTableModel();

        for (ObjectField f : fields) {
            model.addColumn(f.getFieldName());
        }

        for (Object o : objects) {
            fields = ObjectReflector.getFields(o);
            ArrayList<String> all = new ArrayList<>();
            for (ObjectField f : fields) {
                try {
                    all.add(f.getGetter().invoke(f.getO()).toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            model.addRow(all.toArray());
        }

        // force update
        structure_table.setModel(model);
    }

    @Override
    public Object getSelectedItem() {
        if (structure_table.getSelectedRow() != -1) {
            return objects.get(structure_table.getSelectedRow());
        }
        return null;
    }
}