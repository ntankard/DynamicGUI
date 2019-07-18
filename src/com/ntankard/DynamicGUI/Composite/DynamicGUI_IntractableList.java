package com.ntankard.DynamicGUI.Composite;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.Filter.DynamicGUI_Filter;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayList;
import com.ntankard.DynamicGUI.Util.ButtonPanel;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class DynamicGUI_IntractableList<T> extends Updatable.UpdatableJPanel {

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Factories #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public static <T> DynamicGUI_IntractableList newIntractableList(List<T> objects, MemberClass mClass, Updatable master) {
        return newIntractableList(objects, mClass, false, ALWAYS_DISPLAY, master);
    }

    public static <T> DynamicGUI_IntractableList newIntractableList(List<T> objects, MemberClass mClass, int verbosity, Updatable master) {
        return newIntractableList(objects, mClass, false, verbosity, master);
    }

    public static <T> DynamicGUI_IntractableList newIntractableList(List<T> objects, MemberClass mClass, boolean addFilter, Updatable master) {
        return newIntractableList(objects, mClass, addFilter, ALWAYS_DISPLAY, master);
    }

    public static <T> DynamicGUI_IntractableList newIntractableList(List<T> objects, MemberClass mClass, boolean addFilter, int verbosity, Updatable master) {
        return new DynamicGUI_IntractableList<>(objects, mClass, false, addFilter, verbosity, master);
    }

    public static <T> DynamicGUI_IntractableList newIntractableTable(List<T> objects, MemberClass mClass, Updatable master) {
        return newIntractableTable(objects, mClass, false, ALWAYS_DISPLAY, master);
    }

    public static <T> DynamicGUI_IntractableList newIntractableTable(List<T> objects, MemberClass mClass, int verbosity, Updatable master) {
        return newIntractableTable(objects, mClass, false, verbosity, master);
    }

    public static <T> DynamicGUI_IntractableList newIntractableTable(List<T> objects, MemberClass mClass, boolean addFilter, Updatable master) {
        return newIntractableTable(objects, mClass, addFilter, ALWAYS_DISPLAY, master);
    }

    public static <T> DynamicGUI_IntractableList newIntractableTable(List<T> objects, MemberClass mClass, boolean addFilter, int verbosity, Updatable master) {
        return new DynamicGUI_IntractableList<>(objects, mClass, true, addFilter, verbosity, master);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Core ##########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * The master content of the list
     */
    private List<T> base;

    /**
     * The kind of object used to generate this panel
     */
    private MemberClass mClass;

    /**
     * Is the primary display a table or list?
     */
    private boolean isTable;

    /**
     * Should the filter panel be added?
     */
    private boolean addFilter;

    /**
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private int verbosity;

    /**
     * The version of the list with the fillers applied
     */
    private List<T> filtered;

    /**
     * All the predicates for each of the individual controls
     */
    private List<Predicate> predicates;

    /**
     * The buttons to add
     */
    private List<ListControl_Button> buttons = new ArrayList<>();

    /**
     * GUI Objects
     */
    private ButtonPanel buttonPanel;
    private DynamicGUI_Filter filterPanel;
    private DynamicGUI_DisplayList display;

    /**
     * Constructor
     *
     * @param base
     * @param mClass
     * @param isTable
     * @param addFilter
     * @param verbosity
     * @param master
     */
    protected DynamicGUI_IntractableList(List<T> base, MemberClass mClass, boolean isTable, boolean addFilter, int verbosity, Updatable master) {
        super(master);
        this.base = base;
        this.mClass = mClass;
        this.isTable = isTable;
        this.addFilter = addFilter;
        this.verbosity = verbosity;

        this.filtered = new ArrayList<>();
        this.predicates = new ArrayList<>();
        this.buttonPanel = new ButtonPanel();

        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.setBorder(new EmptyBorder(12, 12, 12, 12));
        this.setLayout(new BorderLayout());

        if (isTable) {
            display = DynamicGUI_DisplayList.newStandardDisplayTable(filtered, verbosity, this);
        } else {
            display = DynamicGUI_DisplayList.newStandardDisplayList(filtered, this);
        }
        this.add(display, BorderLayout.CENTER);

        buttonPanel = new ButtonPanel();
        buttons.forEach(listControl_button -> {
            buttonPanel.addButton(listControl_button);
            listControl_button.setControllableList(display);
        });
        this.add(buttonPanel, BorderLayout.SOUTH);

        if (addFilter) {
            filterPanel = new DynamicGUI_Filter(mClass, predicates, verbosity, this);
            this.add(filterPanel, BorderLayout.EAST);
        }

        display.getListSelectionModel().addListSelectionListener(e -> {
            List selected = display.getSelectedItems();

            for (JButton button : buttonPanel.getButtons()) {
                if(button instanceof  ListControl_Button){
                    ListControl_Button lButton = (ListControl_Button)button;
                    lButton.notifySelected(selected == null ? 0 : selected.size());
                }
            }
        });
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Add a button to the panel
     * @param button The button to add
     */
    public void addButton(ListControl_Button button) {
        buttons.add(button);
        createUIComponents();
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
        filtered.clear();

        if (base.size() != 0) {
            List filteredList = base.stream().filter((T o) -> {
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

        display.update();
        if (filterPanel != null) {
            filterPanel.update();
        }
    }

    public static class ListControl_Button extends JButton {

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
        private DynamicGUI_DisplayList coreList;

        /**
         * Constructor
         *
         * @param name The name to put on the button
         */
        public ListControl_Button(String name) {
            this(name,EnableCondition.ANY,true);
        }

        /**
         * Constructor
         *
         * @param name The name to put on the button
         * @param enableCondition In what situation should the button be enabled?
         * @param enableNow What is the default enable state of the button
         */
        public ListControl_Button(String name, EnableCondition enableCondition, boolean enableNow) {
            super(name);
            this.setEnabled(enableNow);
            this.enableCondition  = enableCondition;
        }

        /**
         * Called when the button is added to a DynamicGUI_DisplayList to register the parent
         *
         * @param coreList The list to link to
         */
        public void setControllableList(DynamicGUI_DisplayList coreList) {
            this.coreList = coreList;
        }

        /**
         * Accessor to allow the button (and its action action) to get access to the list it is to act on
         *
         * @return The list containing this button
         */
        public DynamicGUI_DisplayList getCoreList() {
            return coreList;
        }

        //@TODO It might make more sense for this to become a generic "changeable button". Can be useful for error checking in the future
        /**
         * Tell the button that the List it is attached to has had a selection. Update the button based on this
         * @param selected The number of elements selected
         */
        public void notifySelected(int selected){
            switch (enableCondition){
                case ANY:
                    this.setEnabled(true);
                    break;
                case NONE:
                    if(selected == 0){
                        this.setEnabled(true);
                    }else{
                        this.setEnabled(false);
                    }
                    break;
                case SINGLE:
                    if(selected == 1){
                        this.setEnabled(true);
                    }else{
                        this.setEnabled(false);
                    }
                    break;
                case MULTI:
                    if(selected >= 1){
                        this.setEnabled(true);
                    }else{
                        this.setEnabled(false);
                    }
                    break;
            }
        }
    }
}