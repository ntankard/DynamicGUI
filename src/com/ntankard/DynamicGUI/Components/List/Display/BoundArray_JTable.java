package com.ntankard.DynamicGUI.Components.List.Display;

import com.ntankard.DynamicGUI.Components.List.BoundArray_Properties;
import com.ntankard.DynamicGUI.Generator.ObjectField;
import com.ntankard.DynamicGUI.Generator.ObjectReflector;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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

    /**
     * What level of verbosity should be shown? (compared against BoundArray_Properties verbosity)
     */
    private int verbosity;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param objects
     */
    public BoundArray_JTable(ArrayList objects, Updatable master, int verbosity) {
        super(objects, master);
        this.verbosity = verbosity;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        model = new DefaultTableModel();
        structure_table = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                int rendererWidth = component.getPreferredSize().width;
                TableColumn tableColumn = getColumnModel().getColumn(column);
                tableColumn.setPreferredWidth(Math.max(rendererWidth + getIntercellSpacing().width, tableColumn.getPreferredWidth()));
                return component;
            }
        };
        structure_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        structure_table.setAutoCreateRowSorter(true);

        this.setViewportView(structure_table);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    private void addHeaders(Object top, DefaultTableModel model, String pre) throws InvocationTargetException, IllegalAccessException {
        ArrayList<ObjectField> fields = ObjectReflector.getFields(top);

        for (ObjectField f : fields) {
            BoundArray_Properties properties = f.getGetter().getAnnotation(BoundArray_Properties.class);
            if (properties != null) {
                if (properties.verbosityLevel() > verbosity) {
                    continue;
                } else if (properties.partComposite()) {
                    addHeaders(f.getGetter().invoke(f.getO()), model, f.getFieldName() + "_");
                    continue;
                }
            }
            model.addColumn(pre + f.getFieldName());
        }
    }

    private void addRow(Object rowObject, ArrayList<String> rowString) throws InvocationTargetException, IllegalAccessException {
        ArrayList<ObjectField> fields = ObjectReflector.getFields(rowObject);

        // add each cell
        for (ObjectField field : fields) {
            BoundArray_Properties properties = field.getGetter().getAnnotation(BoundArray_Properties.class);
            if (properties != null) {

                // should we skip this cell? or dig deeper into it
                if (properties.verbosityLevel() > verbosity) {
                    continue;
                } else if (properties.partComposite()) {
                    addRow(field.getGetter().invoke(field.getO()), rowString);
                    continue;
                }

            }

            // get standard cell
            String toAdd;
            if (field.getGetter().invoke(field.getO()) instanceof Calendar) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm");
                toAdd = dateFormat.format(((Calendar) field.getGetter().invoke(field.getO())).getTime());
            } else {
                try {
                    toAdd = field.getGetter().invoke(field.getO()).toString();
                } catch (NullPointerException e) {
                    toAdd = "";
                }
            }
            rowString.add(toAdd);
        }
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
        if (objects != null && objects.size() != 0) {

            // try all objects to find one with enough data to generate the header TODO can expand this by building the header in parts
            for (int i = 0; i < objects.size(); i++) {
                model = new DefaultTableModel();
                try {
                    addHeaders(objects.get(i), model, "");
                    break;
                } catch (Exception e) {
                    if (i + 1 == objects.size()) {
                        throw new RuntimeException(e);
                    }
                }
            }

            // add each row
            for (Object rowObject : objects) {
                if (rowObject != null) {
                    ArrayList<String> rowString = new ArrayList<>();
                    try {
                        addRow(rowObject, rowString);
                        model.addRow(rowString.toArray());
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                        //rowString = new ArrayList<>();
                        //rowString.add("Failed Object");
                        // model.addRow(rowString.toArray()); // couldn't parse the full row TODO this can happen if a partComposite field is null ,fix this
                    }
                } else {
                    model.addRow(new String[1]); // no object to write, so write blank
                }
            }

            // force update
            structure_table.setModel(model);
        } else {
            structure_table.setModel(new DefaultTableModel());
        }
    }

    @Override
    public Object getSelectedItem() {
        if (structure_table.getSelectedRow() != -1) {
            return objects.get(structure_table.getSelectedRow());
        }
        return null;
    }
}