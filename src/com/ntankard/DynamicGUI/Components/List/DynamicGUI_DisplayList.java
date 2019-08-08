package com.ntankard.DynamicGUI.Components.List;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.Filter.DynamicGUI_Filter;
import com.ntankard.DynamicGUI.Components.List.Types.DisplayList_JTable;
import com.ntankard.DynamicGUI.Components.Object.DynamicGUI_IntractableObject;
import com.ntankard.DynamicGUI.Util.Swing.Containers.ControllablePanel;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class DynamicGUI_DisplayList<T> extends ControllablePanel<DynamicGUI_DisplayList_Impl, DynamicGUI_Filter> {

    public static <T> DynamicGUI_DisplayList newIntractableTable(List<T> objects, MemberClass mClass, Updatable master) {
        return newIntractableTable(objects, mClass, false, false, ALWAYS_DISPLAY, master);
    }

    public static <T> DynamicGUI_DisplayList newIntractableTable(List<T> objects, MemberClass mClass, int verbosity, Updatable master) {
        return newIntractableTable(objects, mClass, false, false, verbosity, master);
    }

    public static <T> DynamicGUI_DisplayList newIntractableTable(List<T> objects, MemberClass mClass, boolean addFilter, int verbosity, Updatable master) {
        return newIntractableTable(objects, mClass, addFilter, false, verbosity, master);
    }

    public static <T> DynamicGUI_DisplayList newEditIntractableTable(List<T> objects, MemberClass mClass, Updatable master) {
        return newIntractableTable(objects, mClass, false, true, ALWAYS_DISPLAY, master, null);
    }

    public static <T> DynamicGUI_DisplayList newIntractableList(List<T> objects, MemberClass mClass, Updatable master) {
        return newIntractableList(objects, mClass, false, ALWAYS_DISPLAY, master);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Factories #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public static <T> DynamicGUI_DisplayList newIntractableTable(List<T> objects, MemberClass mClass, boolean addFilter, boolean addControl, int verbosity, Updatable master, Object... sources) {
        List predicates = new ArrayList<>();
        List filtered = new ArrayList<>();

        DynamicGUI_DisplayList container = new DynamicGUI_DisplayList(objects, filtered, predicates, master);

        DynamicGUI_DisplayList_Impl main;
        if (addFilter) {
            main = new DisplayList_JTable(filtered, verbosity, container);
            DynamicGUI_Filter control = DynamicGUI_Filter.newFilterPanel(mClass, predicates, verbosity, container);
            container.setMainPanel(main);
            container.setControlPanel(control);
        } else {
            main = new DisplayList_JTable(objects, verbosity, container);
            container.setMainPanel(main);
        }
        // container.setMainPanel(main);

        if (addControl) {
            container.addControlButtons(sources);
        }

        container.update();

        return container;
    }

    public static <T> DynamicGUI_DisplayList newIntractableList(List<T> objects, MemberClass mClass, boolean addControl, int verbosity, Updatable master, Object... sources) {
        List predicates = new ArrayList<>();
        List filtered = new ArrayList<>();

        DynamicGUI_DisplayList container = new DynamicGUI_DisplayList(objects, filtered, predicates, master);

        DynamicGUI_DisplayList_Impl<T> main = new DisplayList_JTable<>(objects, verbosity, container);
        container.setMainPanel(main);

        if (addControl) {
            container.addControlButtons(sources);
        }

        container.update();

        return container;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Core Object ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * The master content of the list
     */
    private List<T> base;

    /**
     * The version of the list with the fillers applied
     */
    private List<T> filtered;

    /**
     * All the predicates for each of the individual controls
     */
    private List<Predicate<T>> predicates;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    private DynamicGUI_DisplayList(List<T> base, List<T> filtered, List<Predicate<T>> predicates, Updatable master) {
        super(master);
        this.base = base;
        this.filtered = filtered;
        this.predicates = predicates;
    }

    /**
     * Get a button panel with the save and cancel button
     *
     * @param sources The source data needed for a child object
     */
    private void addControlButtons(Object[] sources) {
        ListControl_Button newBtn = new ListControl_Button<>("New", this);
        newBtn.addActionListener(e -> {
            List selected = getMainPanel().getSelectedItems();
        });
        addButton(newBtn);

        ListControl_Button editBtn = new ListControl_Button<>("Edit", this, ListControl_Button.EnableCondition.SINGLE, false);
        editBtn.addActionListener(e -> {
            List selected = getMainPanel().getSelectedItems();
            DynamicGUI_IntractableObject.openIntractableObjectFrame(selected.get(0), 0, this, sources);
        });
        addButton(editBtn);

        ListControl_Button deleteBtn = new ListControl_Button<>("Delete", this, ListControl_Button.EnableCondition.MULTI, false);
        deleteBtn.addActionListener(e -> {
            List selected = getMainPanel().getSelectedItems();
        });
        addButton(deleteBtn);
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
        filtered.clear();

        if (base.size() != 0) {
            List filteredList = base.stream().filter(o -> {
                for (Predicate p : predicates) {
                    if (!p.test(o)) {
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());
            filtered.addAll(filteredList);
        } else {
            filtered.addAll(base);
        }

        getMainPanel().update();
        if (getControlPanel() != null) {
            getControlPanel().update();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################ Interface Objects ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public static class ListControl_Button<T> extends JButton implements ListSelectionListener {

        /**
         * How many elements need to be selected for the button to be enabled
         */
        public enum EnableCondition {ANY, NONE, SINGLE, MULTI}

        /**
         * In what situation should the button be enabled?
         */
        private EnableCondition enableCondition;

        /**
         * The list containing this button
         */
        private DynamicGUI_DisplayList_Impl coreList;

        /**
         * Constructor
         *
         * @param name The name to put on the button
         */
        public ListControl_Button(String name, DynamicGUI_DisplayList<T> controllableList) {
            this(name, controllableList, EnableCondition.ANY, true);
        }

        /**
         * Constructor
         *
         * @param name            The name to put on the button
         * @param enableCondition In what situation should the button be enabled?
         * @param enableNow       What is the default enable state of the button
         */
        public ListControl_Button(String name, DynamicGUI_DisplayList<T> controllableList, EnableCondition enableCondition, boolean enableNow) {
            super(name);
            this.setEnabled(enableNow);
            this.enableCondition = enableCondition;
            this.coreList = controllableList.getMainPanel();

            controllableList.getMainPanel().getListSelectionModel().addListSelectionListener(this);
        }

        /**
         * @inheritDoc
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            List selected = coreList.getSelectedItems();
            int noSelected = selected == null ? 0 : selected.size();

            switch (enableCondition) {
                case ANY:
                    this.setEnabled(true);
                    break;
                case NONE:
                    if (noSelected == 0) {
                        this.setEnabled(true);
                    } else {
                        this.setEnabled(false);
                    }
                    break;
                case SINGLE:
                    if (noSelected == 1) {
                        this.setEnabled(true);
                    } else {
                        this.setEnabled(false);
                    }
                    break;
                case MULTI:
                    if (noSelected >= 1) {
                        this.setEnabled(true);
                    } else {
                        this.setEnabled(false);
                    }
                    break;
            }
        }
    }
}
