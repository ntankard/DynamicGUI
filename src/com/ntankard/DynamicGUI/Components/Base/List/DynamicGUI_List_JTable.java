package com.ntankard.DynamicGUI.Components.Base.List;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
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
import java.util.List;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class DynamicGUI_List_JTable<T> extends DynamicGUI_List<T> {

    /**
     * GUI Objects
     */
    private JTable structure_table;

    /**
     * The names of the above objects
     */
    private DefaultTableModel model;

    /**
     * The members used in the list (one field, one column)
     */
    private List<Member> members;

    /**
     * What level of verbosity should be shown? (compared against DynamicGUI_List_Properties verbosity)
     */
    private int verbosity;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @param objects
     */
    protected DynamicGUI_List_JTable(List<T> objects, int verbosity, Updatable master) {
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

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
        try {
            model = new DefaultTableModel();
            members = new ArrayList<>();

            if (getObjects() != null && getObjects().size() != 0) {
                for (T o : getObjects()) {
                    if (o != null) {
                        // One time extract the members
                        if (model.getColumnCount() == 0) {
                            extractMembers(o);
                            members.forEach(member -> model.addColumn(member.getName()));
                        }

                        // Add the row data
                        addRow(o);
                    } else {
                        model.addRow(new String[1]);
                    }
                }
            }

            structure_table.setModel(model);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get all the members to use in the table
     *
     * @param top A non null element to extract the members from
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void extractMembers(Object top) throws InvocationTargetException, IllegalAccessException {
        for (Member f : new MemberClass(top).getMembers()) {
            DynamicGUI_List_Properties properties = f.getGetter().getAnnotation(DynamicGUI_List_Properties.class);
            if (properties != null) {
                if (properties.verbosityLevel() > verbosity) {
                    continue;
                }
            }
            this.members.add(f);
        }
    }

    /**
     * Add an individual row of data
     *
     * @param rowObject The object containing the data to add
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void addRow(Object rowObject) throws InvocationTargetException, IllegalAccessException {
        ArrayList<String> rowString = new ArrayList<>();

        // Add each column
        for (Member member : members) {
            String toAdd;
            Object data = member.getGetter().invoke(rowObject);

            // Parse the data based on its type
            if (data == null) {
                toAdd = "";
            } else if (data instanceof Calendar) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm");
                toAdd = dateFormat.format(((Calendar) member.getGetter().invoke(rowObject)).getTime());
            } else {
                toAdd = data.toString();
            }
            
            rowString.add(toAdd);
        }

        model.addRow(rowString.toArray());
    }

    /**
     * @inheritDoc
     */
    @Override
    protected ListSelectionModel getListSelectionModel() {
        return structure_table.getSelectionModel();
    }

    /**
     * @inheritDoc
     */
    @Override
    protected T getItemFromSelectIndex(int i) {
        return getObjects().get(structure_table.convertRowIndexToModel(i));
    }
}