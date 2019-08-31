package com.ntankard.DynamicGUI.Components.List.Types.Table;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
        members.forEach(member -> orderList.add(new MemberColumn(member, this)));
        orderList.sort(Comparator.comparingInt(o -> o.getOrder()));
    }

    /**
     * @inheritDoc
     */
    @Override
    public String getColumnName(int column) {
        return orderList.get(column).getName();
    }

    /**
     * @inheritDoc
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return orderList.get(columnIndex).getMember().getType();
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
        MemberColumn column = orderList.get(columnIndex);

        Object data;
        try {
            data = column.getMember().getGetter().invoke(rowData.get(rowIndex));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    /**
     * Get a render for this column or null if none is specified
     * @param column The column to get
     * @return A render for this column or null if none is specified
     */
    TableCellRenderer getColumnRenderer(int column) {
        return orderList.get(column).getRenderer();
    }
}
