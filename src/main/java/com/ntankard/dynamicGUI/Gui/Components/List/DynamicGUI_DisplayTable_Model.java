package com.ntankard.dynamicGUI.Gui.Components.List;

import com.ntankard.dynamicGUI.Gui.Components.List.Component.MemberColumn;
import com.ntankard.dynamicGUI.Gui.Components.List.Component.MemberColumn_List;
import com.ntankard.dynamicGUI.Gui.Util.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase_Schema;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DynamicGUI_DisplayTable_Model extends AbstractTableModel implements Updatable {

    /**
     * The members used to make the columns for the table
     */
    private List<MemberColumn> orderList = new ArrayList<>();

    /**
     * The data used to populate the rowData of the table
     */
    private final List<DataObject> rowData;

    /**
     * The parent of this object to be notified if data changes
     */
    private Updatable master;

    /**
     * Constructor
     *
     * @param aClass    The kind of object used to generate this panel
     * @param rowData   The list of objects to display
     * @param verbosity What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param master    The top level GUI
     */
    public DynamicGUI_DisplayTable_Model(Class aClass, List rowData, int verbosity, Updatable master) {
        this.rowData = rowData;
        this.master = master;

        List<DataField_Schema<?>> dataFieldSchemas = TrackingDatabase_Schema.getFieldContainer(aClass).getVerbosityDataFields(verbosity);
        for (DataField_Schema<?> dataFieldSchema : dataFieldSchemas) {
            MemberColumn column;
            if (dataFieldSchema.getSource() != null) {
                column = new MemberColumn_List(dataFieldSchema, this);
            } else {
                column = new MemberColumn(dataFieldSchema, this);
            }

            orderList.add(column);
        }
        orderList.sort(Comparator.comparingInt(MemberColumn::getOrder));
    }

    /**
     * Set A user set source for the locale
     *
     * @param numberFormatSource A user set source for the locale
     */
    public void setNumberFormatSource(CurrencyDecoder_NumberFormatSource numberFormatSource) {
        for (MemberColumn col : orderList) {
            col.setNumberFormatSource(numberFormatSource);
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
     * Get the members used to make the columns for the table
     *
     * @return The members used to make the columns for the table
     */
    public List<MemberColumn> getOrderList() {
        return orderList;
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
        if (orderList.get(columnIndex).getDataFieldSchema().getType().equals(Double.class)) {
            return String.class;
        }
        return orderList.get(columnIndex).getDataFieldSchema().getType();
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
        return rowData.get(rowIndex).get(column.getDataFieldSchema().getIdentifierName());
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
        Object decodedValue = orderList.get(columnIndex).encode(aValue);
        rowData.get(rowIndex).set(orderList.get(columnIndex).getDataFieldSchema().getIdentifierName(), decodedValue);
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
