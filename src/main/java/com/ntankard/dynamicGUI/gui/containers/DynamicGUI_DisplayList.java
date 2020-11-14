package com.ntankard.dynamicGUI.gui.containers;

import com.ntankard.dynamicGUI.gui.components.list.DynamicGUI_DisplayTable_Impl;
import com.ntankard.dynamicGUI.gui.util.containers.ControllablePanel;
import com.ntankard.dynamicGUI.gui.util.decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.coreObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database_Schema;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ntankard.javaObjectDatabase.coreObject.field.properties.Display_Properties.ALWAYS_DISPLAY;

public class DynamicGUI_DisplayList<T extends DataObject> extends ControllablePanel<DynamicGUI_DisplayTable_Impl<T>, DynamicGUI_Filter<T>> {

    /**
     * The master content of the list
     */
    protected List<T> base;

    /**
     * The version of the list with the fillers applied
     */
    private final List<T> filtered = new ArrayList<>();

    /**
     * All the predicates for each of the individual controls
     */
    private List<Predicate<T>> predicates = null;

    /**
     * The kind of object used to generate this table
     */
    private final Class<T> aClass;

    /**
     * Set A user set source for the locale
     */
    private CurrencyDecoder_NumberFormatSource localeSource;

    /**
     * What level of verbosity should be shown?
     */
    private int verbosity = ALWAYS_DISPLAY;

    /**
     * Comparator used to sort the list before displaying
     */
    private Comparator<T> comparator = null;

    /**
     * The controller used to general new objects (if provided)
     */
    private ElementController<T> controller = null;

    /**
     * The new button, stored so it can be disabled if cant be used
     */
    private ListControl_Button<T> newBtn = null;

    /**
     * The new edit button, stored so it can be disabled if cant be used
     */
    private ListControl_Button<T> newEditBtn = null;

    /**
     * Core Schema
     */
    private final Database_Schema schema;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public DynamicGUI_DisplayList(Database_Schema schema, List<T> base, Class<T> aClass, Updatable master) {
        super(master);
        this.schema = schema;
        this.base = base;
        this.aClass = aClass;

        setMainPanel(new DynamicGUI_DisplayTable_Impl<>(schema, aClass, filtered, this));
    }

    /**
     * Set a user set source for the locale, numberFormat used if not set
     *
     * @param localeSource A user set source for the locale, numberFormat used if not set
     * @return This
     */
    @SuppressWarnings("UnusedReturnValue")
    public DynamicGUI_DisplayList<T> setLocaleSource(CurrencyDecoder_NumberFormatSource localeSource) {
        this.localeSource = localeSource;
        getMainPanel().setLocaleSource(localeSource);
        update();
        return this;
    }

    /**
     * Set what level of verbosity should be shown?
     *
     * @param verbosity What level of verbosity should be shown?
     * @return This
     */
    public DynamicGUI_DisplayList<T> setVerbosity(int verbosity) {
        this.verbosity = verbosity;
        getMainPanel().setVerbosity(verbosity);
        if (getControlPanel() != null) {
            getControlPanel().setVerbosity(verbosity);
        }
        update();
        return this;
    }

    /**
     * Add a filter panel
     *
     * @return This
     */
    @SuppressWarnings("UnusedReturnValue")
    public DynamicGUI_DisplayList<T> addFilter() {
        predicates = new ArrayList<>();
        setControlPanel(new DynamicGUI_Filter<>(schema, aClass, predicates, this));
        getControlPanel().setVerbosity(verbosity);

        return this;
    }

    /**
     * Get a button panel with the save and cancel button
     *
     * @param controller The main controller
     */
    @SuppressWarnings("UnusedReturnValue")
    public DynamicGUI_DisplayList<T> addControlButtons(ElementController<T> controller) {
        this.controller = controller;
        if (controller != null) {
            newBtn = new ListControl_Button<>("New", this);
            newBtn.addActionListener(e -> {
                T newObj = controller.newElement();
                controller.addElement(newObj);
                notifyUpdate();
            });
            addButton(newBtn);
        }

        if (controller != null) {
            newEditBtn = new ListControl_Button<>("New Edit", this);
            newEditBtn.addActionListener(e -> {
                T newObj = controller.newElement();
                DynamicGUI_IntractableObject<?> core = new DynamicGUI_IntractableObject<>(newObj, this)
                        .setLocaleSource(localeSource)
                        .setVerbosity(verbosity);
                if (DynamicGUI_IntractableObject.openIntractableObjectDialog(core)) {
                    controller.addElement(newObj);
                }
                notifyUpdate();
            });
            addButton(newEditBtn);
        }

        ListControl_Button<T> editBtn = new ListControl_Button<>("Edit", this, ListControl_Button.EnableCondition.SINGLE, false);
        editBtn.addActionListener(e -> {
            List<? extends DataObject> selected = getMainPanel().getSelectedItems();
            DynamicGUI_IntractableObject.openIntractableObjectDialog(new DynamicGUI_IntractableObject<>(selected.get(0), this)
                    .setLocaleSource(localeSource)
                    .setVerbosity(verbosity));
        });
        addButton(editBtn);

        if (controller != null) {
            ListControl_Button<T> deleteBtn = new ListControl_Button<>("Delete", this, ListControl_Button.EnableCondition.MULTI, false);
            deleteBtn.addActionListener(e -> {
                List<T> selected = getMainPanel().getSelectedItems();
                for (T del : selected) {
                    controller.deleteElement(del);
                }
                notifyUpdate();
            });
            addButton(deleteBtn);
        }

        return this;
    }

    /**
     * Set the comparator used to sort the list before displaying
     *
     * @param comparator Comparator used to sort the list before displaying
     * @return This
     */
    @SuppressWarnings("unused")
    public DynamicGUI_DisplayList<T> setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
        update();
        return this;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        filtered.clear();

        if (base.size() != 0 && predicates != null) {
            List<T> filteredList = base.stream().filter(o -> {
                for (Predicate<T> p : predicates) {
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

        if (comparator != null) {
            filtered.sort(comparator);
        }

        if (newBtn != null && controller != null) {
            newBtn.setEnabled(controller.canCreate());
        }
        if (newEditBtn != null && controller != null) {
            newEditBtn.setEnabled(controller.canCreate());
        }

        super.update();
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################ Interface Objects ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Interface to interface with objects in the database
     */
    public interface ElementController<T> {
        /**
         * Remove this item from the database
         *
         * @param toDel The item to delete
         */
        void deleteElement(T toDel);

        /**
         * Get a valid new object of T type but don't add it to the database
         *
         * @return The new value
         */
        T newElement();

        /**
         * Add an element to the database. This will always be a modified version of the object received from newElement
         *
         * @param newObj The object to add
         */
        void addElement(T newObj);

        /**
         * Check that the controller is in a valid state to generate a new object
         *
         * @return True if the controller is in a valid state to generate a new object
         */
        boolean canCreate();
    }

    public static class ListControl_Button<T extends DataObject> extends JButton implements ListSelectionListener {

        /**
         * How many elements need to be selected for the button to be enabled
         */
        public enum EnableCondition {ANY, NONE, SINGLE, MULTI}

        /**
         * In what situation should the button be enabled?
         */
        private ListControl_Button.EnableCondition enableCondition;

        /**
         * The list containing this button
         */
        private DynamicGUI_DisplayTable_Impl<T> coreList;

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
            List<?> selected = coreList.getSelectedItems();
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
