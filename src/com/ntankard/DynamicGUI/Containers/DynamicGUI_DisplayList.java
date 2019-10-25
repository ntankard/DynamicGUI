package com.ntankard.DynamicGUI.Containers;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayTable_Impl;
import com.ntankard.DynamicGUI.Util.Containers.ControllablePanel;
import com.ntankard.DynamicGUI.Util.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class DynamicGUI_DisplayList<T> extends ControllablePanel<DynamicGUI_DisplayTable_Impl, DynamicGUI_Filter> {

    /**
     * The master content of the list
     */
    private List<T> base;

    /**
     * The version of the list with the fillers applied
     */
    private List<T> filtered = new ArrayList<>();

    /**
     * All the predicates for each of the individual controls
     */
    private List<Predicate<T>> predicates = null;

    /**
     * The kind of object used to generate this table
     */
    private MemberClass mClass;

    /**
     * Set A user set source for the locale
     */
    private CurrencyDecoder_NumberFormatSource localeSource;
    /**
     * Sources of data that can be set for various objects
     */
    private Object[] sources;

    /**
     * What level of verbosity should be shown?
     */
    private int verbosity = ALWAYS_DISPLAY;

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public DynamicGUI_DisplayList(List<T> base, MemberClass mClass, Updatable master) {
        super(master);
        this.base = base;
        this.mClass = mClass;

        setMainPanel(new DynamicGUI_DisplayTable_Impl<>(mClass, filtered, this));
    }

    /**
     * Set a user set source for the locale, numberFormat used if not set
     *
     * @param localeSource A user set source for the locale, numberFormat used if not set
     * @return This
     */
    public DynamicGUI_DisplayList<T> setLocaleSource(CurrencyDecoder_NumberFormatSource localeSource) {
        this.localeSource = localeSource;
        getMainPanel().setLocaleSource(localeSource);
        update();
        return this;
    }

    /**
     * Set the sources of data that can be set for various objects
     *
     * @param sources Sources of data that can be set for various objects
     * @return This
     */
    public DynamicGUI_DisplayList<T> setSources(Object... sources) {
        this.sources = sources;
        getMainPanel().setSources(sources);
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
    public DynamicGUI_DisplayList<T> addFilter() {
        predicates = new ArrayList<>();
        setControlPanel(new DynamicGUI_Filter<>(mClass, predicates, this));
        getControlPanel().setVerbosity(verbosity);

        return this;
    }

    /**
     * Get a button panel with the save and cancel button
     *
     * @param controller The main controller
     */
    public DynamicGUI_DisplayList<T> addControlButtons(ElementController<T> controller) {
        if (controller != null) {
            ListControl_Button newBtn = new ListControl_Button<>("New", this);
            newBtn.addActionListener(e -> {
                T newObj = controller.newElement();
                controller.addElement(newObj);
                notifyUpdate();
            });
            addButton(newBtn);
        }

        if (controller != null) {
            ListControl_Button newEditBtn = new ListControl_Button<>("New Edit", this);
            newEditBtn.addActionListener(e -> {
                T newObj = controller.newElement();
                DynamicGUI_IntractableObject<?> core = new DynamicGUI_IntractableObject<>(newObj, this)
                        .setLocaleSource(localeSource)
                        .setSources(sources)
                        .setVerbosity(verbosity);
                if (DynamicGUI_IntractableObject.openIntractableObjectDialog(core)) {
                    controller.addElement(newObj);
                }
                notifyUpdate();
            });
            addButton(newEditBtn);
        }

        ListControl_Button editBtn = new ListControl_Button<>("Edit", this, ListControl_Button.EnableCondition.SINGLE, false);
        editBtn.addActionListener(e -> {
            List selected = getMainPanel().getSelectedItems();
            DynamicGUI_IntractableObject.openIntractableObjectDialog(new DynamicGUI_IntractableObject<>(selected.get(0), this)
                    .setLocaleSource(localeSource)
                    .setSources(sources)
                    .setVerbosity(verbosity));
        });
        addButton(editBtn);

        if (controller != null) {
            ListControl_Button deleteBtn = new ListControl_Button<>("Delete", this, ListControl_Button.EnableCondition.MULTI, false);
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
     * @inheritDoc
     */
    @Override
    public void update() {
        filtered.clear();

        if (base.size() != 0 && predicates != null) {
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
    }

    public static class ListControl_Button<T> extends JButton implements ListSelectionListener {

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
        private DynamicGUI_DisplayTable_Impl coreList;

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
