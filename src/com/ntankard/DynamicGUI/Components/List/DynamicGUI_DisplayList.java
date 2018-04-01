package com.ntankard.DynamicGUI.Components.List;

import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.TRACE_DISPLAY;

public abstract class DynamicGUI_DisplayList<T> extends Updatable.UpdatableJScrollPane {

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Factories #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public static <T> DynamicGUI_DisplayList newStandardDisplayList(List<T> objects, Updatable master) {
        return new DisplayList_JList<>(objects, master);
    }

    public static <T> DynamicGUI_DisplayList newStandardDisplayTable(List<T> objects, Updatable master) {
        return newStandardDisplayTable(objects, TRACE_DISPLAY, master);
    }

    public static <T> DynamicGUI_DisplayList newStandardDisplayTable(List<T> objects, int verbosity, Updatable master) {
        return new DisplayList_JTable<>(objects, verbosity, master);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Core ##########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * The master content of the list
     */
    private List<T> objects;

    /**
     * @param objects
     * @param master
     */
    protected DynamicGUI_DisplayList(List<T> objects, Updatable master) {
        super(master);
        this.objects = objects;
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

    //------------------------------------------------------------------------------------------------------------------
    //################################################ To Extended #####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get the ListSelectionModel used by the child
     *
     * @return The ListSelectionModel used by the child
     */
    protected abstract ListSelectionModel getListSelectionModel();

    /**
     * Get the element tired to a index in the list (usual the same but can be different if the display is sorted
     *
     * @param i The index to get
     * @return The element at that index
     */
    protected abstract T getItemFromSelectIndex(int i);

    /**
     * Get the objects
     *
     * @return The objects
     */
    protected List<T> getObjects() {
        return objects;
    }
}