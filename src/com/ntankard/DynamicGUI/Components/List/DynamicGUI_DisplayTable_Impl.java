package com.ntankard.DynamicGUI.Components.List;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.Component.MemberColumn;
import com.ntankard.DynamicGUI.Components.List.Component.MemberColumn_List;
import com.ntankard.DynamicGUI.Components.Object.DynamicGUI_IntractableObject_Impl;
import com.ntankard.DynamicGUI.Util.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.DynamicGUI.Util.Table.TableColumnAdjuster;
import com.ntankard.DynamicGUI.Util.Update.UpdatableJScrollPane;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

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
    private final MemberClass mClass;

    /**
     * Sources of data that can be set for various rowData
     */
    private Object[] sources;

    /**
     * TableColumnAdjuster used to shrink the table
     */
    private TableColumnAdjuster tableColumnAdjuster;

    /**
     * Constructor
     *
     * @param mClass  The kind of object used to generate this panel
     * @param rowData The list of rowData to display
     * @param master  The parent of this object to be notified if data changes
     */
    public DynamicGUI_DisplayTable_Impl(MemberClass mClass, List<T> rowData, Updatable master) {
        super(master);
        this.rowData = rowData;
        this.mClass = mClass;
        this.verbosity = ALWAYS_DISPLAY;
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
     * Set the sources of data that can be set for various rowData
     *
     * @param sources Sources of data that can be set for various rowData
     * @return This
     */
    public DynamicGUI_DisplayTable_Impl<T> setSources(Object... sources) {
        this.sources = sources;
        createUIComponents();
        update();
        return this;
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
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
        model = new DynamicGUI_DisplayTable_Model(mClass, getRowData(), verbosity, this, sources);

        structure_table.setModel(model);

        for (MemberColumn column : model.getOrderList()) {
            if (column instanceof MemberColumn_List) {
                int index = model.getOrderList().indexOf(column);
                TableColumn cell = structure_table.getColumnModel().getColumn(index);
                cell.setCellEditor(new DefaultCellEditor(((MemberColumn_List) column).getCellComboBox()));
            }
        }

        structure_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableColumnAdjuster = new TableColumnAdjuster(structure_table);

        structure_table.setAutoCreateRowSorter(true);

        this.setViewportView(structure_table);
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
     * Set a user set source for the locale
     *
     * @param localeSource A user set source for the locale
     */
    public void setNumberFormatSource(CurrencyDecoder_NumberFormatSource localeSource) {
        model.setNumberFormatSource(localeSource);
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
