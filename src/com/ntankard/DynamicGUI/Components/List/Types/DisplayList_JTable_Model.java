package com.ntankard.DynamicGUI.Components.List.Types;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.ntankard.ClassExtension.DisplayProperties.*;

public class DisplayList_JTable_Model extends AbstractTableModel {

    /**
     * The members used to make the columns for the table
     */
    private List<MemberColumn> orderList = new ArrayList<>();

    /**
     * The data used to populate the rowData of the table
     */
    private final List rowData;

    /**
     * Constructor
     *
     * @param mClass    The kind of object used to generate this panel
     * @param rowData   The list of objects to display
     * @param verbosity What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    DisplayList_JTable_Model(MemberClass mClass, List rowData, int verbosity) {
        this.rowData = rowData;
        List<Member> members = mClass.getVerbosityMembers(verbosity);
        members.forEach(member -> orderList.add(new MemberColumn(member)));
        orderList.sort(Comparator.comparingInt(o -> o.order));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getColumnName(int column) {
        return orderList.get(column).name;
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
        return orderList.size();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String toAdd;
        MemberColumn column = orderList.get(columnIndex);
        Object data;

        try {
            data = column.member.getGetter().invoke(rowData.get(rowIndex));
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
            if (column.format.equals(Format.AUD)) {
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
                toAdd = format.format(data);
            } else if (column.format.equals(Format.YEN)) {
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.JAPAN);
                toAdd = format.format(data);
            } else {
                DecimalFormat df2 = new DecimalFormat("#.##");
                toAdd = df2.format(data);
            }
        } else {
            toAdd = data.toString();
        }

        return toAdd;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Util classes #####################################################
    //------------------------------------------------------------------------------------------------------------------

    private class MemberColumn {

        /**
         * The core member
         */
        private Member member;

        /**
         * The display order, Integer.MAX_VALUE if none is set
         */
        private int order = Integer.MAX_VALUE;

        /**
         * The display name of the column
         */
        private String name = "";

        /**
         * Any special format instructions other than the class type
         */
        private Format format = Format.NONE;

        /**
         * Constructor, parameters are set from the DisplayProperties set to the member
         *
         * @param member The member this column is based around
         */
        MemberColumn(Member member) {
            this.member = member;

            DisplayProperties properties = this.member.getGetter().getAnnotation(DisplayProperties.class);
            if (properties != null) {
                order = properties.order();
                name = properties.name();
                format = properties.format();
            }

            if (name.equals("")) {
                name = member.getName();
            }
        }
    }
}
