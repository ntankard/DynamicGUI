package com.ntankard.dynamicGUI.gui.components.filter.component;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * A MemberFilter used to filter double with either an exact match
 */
public class MemberFilter_Enum extends MemberFilter {

    /**
     * The values to match
     */
    private ArrayList<Object> selected = new ArrayList<>();

    /**
     * Constructor
     *
     * @param dataFieldSchema The DataField that this panel is built around
     * @param master          The top level GUI
     */
    public MemberFilter_Enum(DataField_Schema dataFieldSchema, Updatable master) {
        super(dataFieldSchema, master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getDataFieldSchema().getDisplayName()));
        this.setLayout(new BorderLayout());

        Object[] possibleValues = getDataFieldSchema().getType().getEnumConstants();

        JList<Object> list = new JList<>(possibleValues);

        ListSelectionModel listSelectionModel = list.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listSelectionModel.addListSelectionListener(e -> {
            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            selected.clear();
            if (!lsm.isSelectionEmpty()) {
                // Find out which indexes are selected.
                int minIndex = lsm.getMinSelectionIndex();
                int maxIndex = lsm.getMaxSelectionIndex();
                for (int i = minIndex; i <= maxIndex; i++) {
                    if (lsm.isSelectedIndex(i)) {
                        selected.add(possibleValues[i]);
                    }
                }
            }
            notifyUpdate();
        });
        JScrollPane listPane = new JScrollPane(list);

        this.add(listPane, BorderLayout.CENTER);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public Predicate<? extends DataObject> getPredicate() {
        return o -> {
            if (selected == null || selected.size() == 0) {
                return true;
            }
            return selected.contains(o.get(getDataFieldSchema().getIdentifierName()));
        };
    }
}
