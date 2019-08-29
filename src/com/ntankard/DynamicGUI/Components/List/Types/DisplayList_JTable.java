package com.ntankard.DynamicGUI.Components.List.Types;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.ClassExtension.MemberProperties;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList_Impl;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class DisplayList_JTable<T> extends DynamicGUI_DisplayList_Impl<T> {

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
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private int verbosity;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param objects   The list of objects to display
     * @param verbosity What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param master    The parent of this object to be notified if data changes
     */
    public DisplayList_JTable(List<T> objects, int verbosity, Updatable master) {
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
     * Add an individual row of data
     *
     * @param rowObject The object containing the data to add
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void addRow(Object rowObject) throws InvocationTargetException, IllegalAccessException {
        ArrayList<String> rowString = new ArrayList<>();
        DecimalFormat df2 = new DecimalFormat("#.##");

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
            } else if (data instanceof Double){
                if(member.getFormat().equals(MemberProperties.Format.AUD)){
                    NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
                    toAdd = format.format(data);
                }else if(member.getFormat().equals(MemberProperties.Format.YEN)){
                    NumberFormat format = NumberFormat.getCurrencyInstance(Locale.JAPAN);
                    toAdd = format.format(data);
                }else{
                    toAdd = df2.format(data);
                }
            } else {
                toAdd = data.toString();
            }

            rowString.add(toAdd);
        }

        model.addRow(rowString.toArray());
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void update() {
        try {
            model = new DefaultTableModel();
            members = new ArrayList<>();

            if (getObjects() != null && getObjects().size() != 0) {
                for (T o : getObjects()) {
                    if (o != null) {
                        // One time extract the members
                        if (model.getColumnCount() == 0) {
                            members = new MemberClass(o).getVerbosityMembers(verbosity);
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
     * @inheritDoc
     */
    @Override
    public ListSelectionModel getListSelectionModel() {
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
