package com.ntankard.dynamicGUI.gui.components.list;

import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.dynamicGUI.gui.components.list.component.MemberColumn;
import com.ntankard.dynamicGUI.gui.components.list.component.MemberColumn_List;
import com.ntankard.dynamicGUI.gui.util.decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.dynamicGUI.gui.util.table.TableColumnAdjuster;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.dynamicGUI.gui.util.update.UpdatableJScrollPane;
import com.ntankard.javaObjectDatabase.dataField.properties.Display_Properties;
import com.ntankard.javaObjectDatabase.database.Database_Schema;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DynamicGUI_DisplayTable_Impl<T> extends UpdatableJScrollPane {

    /**
     * The master content of the list
     */
    private List<T> rowData;

    /**
     * GUI Objects
     */
    private JTable structure_table;

    /**
     * The names of the above rowData
     */
    private DynamicGUI_DisplayTable_Model model;

    /**
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private int verbosity;

    /**
     * The kind of object used to generate this table
     */
    private Class<T> aClass;

    /**
     * TableColumnAdjuster used to shrink the table
     */
    private TableColumnAdjuster tableColumnAdjuster;

    /**
     * Set A user set source for the locale
     */
    private CurrencyDecoder_NumberFormatSource localeSource;

    // Core schema
    private final Database_Schema schema;

    /**
     * Constructor
     *
     * @param aClass  The kind of object used to generate this panel
     * @param rowData The list of rowData to display
     * @param master  The parent of this object to be notified if data changes
     */
    public DynamicGUI_DisplayTable_Impl(Database_Schema schema, Class<T> aClass, List<T> rowData, Updatable master) {
        super(master);
        this.schema = schema;
        this.rowData = rowData;
        this.aClass = aClass;
        this.verbosity = Display_Properties.ALWAYS_DISPLAY;
        createUIComponents();
        update();
    }

    /**
     * Set what level of verbosity should be shown?
     *
     * @param verbosity What level of verbosity should be shown?
     * @return This
     */
    public DynamicGUI_DisplayTable_Impl<T> setVerbosity(int verbosity) {
        this.verbosity = verbosity;
        createUIComponents();
        update();
        return this;
    }

    /**
     * Set a user set source for the locale
     *
     * @param localeSource A user set source for the locale
     */
    public DynamicGUI_DisplayTable_Impl<T> setLocaleSource(CurrencyDecoder_NumberFormatSource localeSource) {
        this.localeSource = localeSource;
        createUIComponents();
        update();
        return this;
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {


        if (structure_table == null) {
            structure_table = new JTable() {
                @Override
                public TableCellRenderer getCellRenderer(int row, int column) {
                    TableCellRenderer renderer = model.getColumnRenderer(column);
                    if (renderer != null) {
                        return renderer;
                    }
                    return super.getCellRenderer(row, column);
                }
            };
        }
        model = new DynamicGUI_DisplayTable_Model(schema, aClass, getRowData(), verbosity, this);

        structure_table.setModel(model);

        for (MemberColumn column : model.getOrderList()) {
            if (column instanceof MemberColumn_List) {
                int index = model.getOrderList().indexOf(column);
                TableColumn cell = structure_table.getColumnModel().getColumn(index);
                cell.setCellEditor(new DefaultCellEditor(new JComboBox<>()) {
                    @Override
                    @SuppressWarnings("unchecked")
                    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int columnIndex) {
                        DataField_Schema<?> dataFieldSchema = column.getDataFieldSchema();
                        List<Object> options;
                        try {
                            options = (List) dataFieldSchema.getSource().invoke(model.getRowObject(rowIndex), dataFieldSchema.getType(), dataFieldSchema.getDisplayName());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }

                        JComboBox<Object> superCombo = (JComboBox<Object>) super.getTableCellEditorComponent(table, value, isSelected, rowIndex, columnIndex);
                        superCombo.setModel(new DefaultComboBoxModel<>(options.toArray()));
                        return superCombo;
                    }
                });
            }
        }

        structure_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableColumnAdjuster = new TableColumnAdjuster(structure_table);

        structure_table.setAutoCreateRowSorter(true);

        this.setViewportView(structure_table);

        model.setNumberFormatSource(localeSource);
    }

    /**
     * Get the items in the list that the user has selected
     *
     * @return The items in the list that the user has selected
     */
    public List<T> getSelectedItems() {
        ListSelectionModel lsm = getListSelectionModel();
        List<T> toReturn = new ArrayList<>();

        if (!lsm.isSelectionEmpty()) {
            // Find out which indexes are selected.
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                    toReturn.add(getItemFromSelectIndex(i));
                }
            }
        }

        return toReturn;
    }

    /**
     * Get the ListSelectionModel used by the child
     *
     * @return The ListSelectionModel used by the child
     */
    public ListSelectionModel getListSelectionModel() {
        return structure_table.getSelectionModel();
    }


    /**
     * Get the element tired to a index in the list (usual the same but can be different if the display is sorted
     *
     * @param i The index to get
     * @return The element at that index
     */
    private T getItemFromSelectIndex(int i) {
        return getRowData().get(structure_table.convertRowIndexToModel(i));
    }

    /**
     * Get the rowData
     *
     * @return The rowData
     */
    private List<T> getRowData() {
        return rowData;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ To Extended #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        model.update();
        tableColumnAdjuster.adjustColumns();
    }
}
