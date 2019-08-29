package com.ntankard.DynamicGUI.Components.List.Types;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.ClassExtension.MemberProperties;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DisplayList_JTable_Model<T> extends AbstractTableModel {

    /**
     * The members used to make the columns for the table
     */
    private final List<Member> members;

    /**
     * The data used to populate the rowData of the table
     */
    private final List<T> rowData;

    /**
     * Constructor
     *
     * @param mClass    The kind of object used to generate this panel
     * @param rowData   The list of objects to display
     * @param verbosity What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    DisplayList_JTable_Model(MemberClass mClass, List<T> rowData, int verbosity) {
        this.rowData = rowData;
        members = mClass.getVerbosityMembers(verbosity);
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getColumnName(int column) {
        return members.get(column).getName();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getRowCount() {
        return rowData.size();
    }

    /**
     * @inheritDoc
     */
    @Override
    public int getColumnCount() {
        return members.size();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String toAdd;
        Member member = members.get(columnIndex);
        DecimalFormat df2 = new DecimalFormat("#.##");
        Object data;
        try {
            data = member.getGetter().invoke(rowData.get(rowIndex));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        // Parse the data based on its type
        if (data == null) {
            toAdd = "";
        } else if (data instanceof Calendar) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM YYYY HH:mm");
            toAdd = dateFormat.format(((Calendar) data).getTime());
        } else if (data instanceof Double) {
            if (member.getFormat().equals(MemberProperties.Format.AUD)) {
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
                toAdd = format.format(data);
            } else if (member.getFormat().equals(MemberProperties.Format.YEN)) {
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.JAPAN);
                toAdd = format.format(data);
            } else {
                toAdd = df2.format(data);
            }
        } else {
            toAdd = data.toString();
        }

        return toAdd;
    }
}
