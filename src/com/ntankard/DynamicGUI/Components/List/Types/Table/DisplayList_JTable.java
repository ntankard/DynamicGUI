package com.ntankard.DynamicGUI.Components.List.Types.Table;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList_Impl;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.CurrencyDecoder_LocaleSource;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.util.List;

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
    private DisplayList_JTable_Model model;

    /**
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private final int verbosity;

    /**
     * The kind of object used to generate this table
     */
    private final MemberClass mClass;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param mClass    The kind of object used to generate this panel
     * @param rowData   The list of objects to display
     * @param verbosity What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param master    The parent of this object to be notified if data changes
     */
    public DisplayList_JTable(MemberClass mClass, List<T> rowData, int verbosity, Updatable master) {
        super(rowData, master);
        this.mClass = mClass;
        this.verbosity = verbosity;
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        model = new DisplayList_JTable_Model(mClass, getObjects(), verbosity, this);

        structure_table = new JTable(model) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                TableCellRenderer renderer = model.getColumnRenderer(column);
                if (renderer != null) {
                    return renderer;
                }
                return super.getCellRenderer(row, column);
            }
        };

        structure_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        structure_table.setAutoCreateRowSorter(true);

        this.setViewportView(structure_table);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void setLocaleInspector(CurrencyDecoder_LocaleSource localeSource) {
        model.setLocaleInspector(localeSource);
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void update() {
        model.update();
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
