package com.ntankard.DynamicGUI.Components.Base.Filter.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
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
     * {@inheritDoc}
     */
    public MemberFilter_Enum(Member baseMember, Updatable master) {
        super(baseMember,master);
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(getBaseMember().getName()));

        Object[] possibleValues = getBaseMember().getType().getEnumConstants();

        JList<Object> list = new JList<>(possibleValues);

        ListSelectionModel listSelectionModel = list.getSelectionModel();
        listSelectionModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listSelectionModel.addListSelectionListener(e -> {
            ListSelectionModel lsm = (ListSelectionModel)e.getSource();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Predicate getPredicate() {
        return o -> {
            if (selected == null || selected.size() == 0) {
                return true;
            }
            try {
                return selected.contains(getBaseMember().getGetter().invoke(o));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };
    }
}