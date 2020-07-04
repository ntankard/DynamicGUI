package com.ntankard.DynamicGUI.Components.Filter.Component;

import com.ntankard.CoreObject.CoreObject;
import com.ntankard.CoreObject.Field.DataField;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

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
     * @param dataField The DataField that this panel is built around
     * @param master    The top level GUI
     */
    public MemberFilter_Enum(DataField dataField, Updatable master) {
        super(dataField, master);
        createUIComponents();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setBorder(BorderFactory.createTitledBorder(getDataField().getDisplayName()));
        this.setLayout(new BorderLayout());

        Object[] possibleValues = getDataField().getType().getEnumConstants();

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
     * {@inheritDoc}
     */
    @Override
    public Predicate<? extends CoreObject> getPredicate() {
        return o -> {
            if (selected == null || selected.size() == 0) {
                return true;
            }
            return selected.contains(o.get(getDataField().getIdentifierName()));
        };
    }
}
