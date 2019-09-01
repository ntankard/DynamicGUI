package com.ntankard.DynamicGUI.Components.List.Types.Table;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.CurrencyDecoder_LocaleSource;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DisplayList_JTable_Model extends AbstractTableModel implements Updatable {

    /**
     * The members used to make the columns for the table
     */
    private List<MemberColumn> orderList = new ArrayList<>();

    /**
     * The data used to populate the rowData of the table
     */
    private final List rowData;

    /**
     * The parent of this object to be notified if data changes
     */
    private Updatable master;

    /**
     * Constructor
     *
     * @param mClass    The kind of object used to generate this panel
     * @param rowData   The list of objects to display
     * @param verbosity What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    DisplayList_JTable_Model(MemberClass mClass, List rowData, int verbosity, Updatable master) {
        this.rowData = rowData;
        this.master = master;
        List<Member> members = mClass.getVerbosityMembers(verbosity);
        members.forEach(member -> orderList.add(new MemberColumn(member, this)));
        orderList.sort(Comparator.comparingInt(MemberColumn::getOrder));
    }

    /**
     * Set A user set source for the locale
     *
     * @param localeSource A user set source for the locale
     */
    public void setLocaleInspector(CurrencyDecoder_LocaleSource localeSource) {
        for (MemberColumn col : orderList) {
            col.setLocaleInspector(localeSource);
        }
    }

    /**
     * Get a render for this column or null if none is specified
     *
     * @param column The column to get
     * @return A render for this column or null if none is specified
     */
    public TableCellRenderer getColumnRenderer(int column) {
        return orderList.get(column).getRenderer();
    }

    /**
     * Get data fora  specific row
     *
     * @param row The row to get
     * @return The object populating that row
     */
    public Object getRowObject(int row) {
        return rowData.get(row);
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
     * @inheritDoc
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return orderList.get(columnIndex).isEditable();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            Object decodedValue = orderList.get(columnIndex).encode(aValue);
            orderList.get(columnIndex).getMember().getSetter().invoke(rowData.get(rowIndex), decodedValue);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Object a = "error value";
            double b = (Double) a;
        }
        notifyUpdate();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        fireTableDataChanged();
    }

    /**
     * @inheritDoc
     */
    @Override
    public void notifyUpdate() {
        master.notifyUpdate();
    }
}
