package com.ntankard.DynamicGUI.Components.List.Display;

import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BoundArray<T> extends JScrollPane implements Updatable {

    /**
     * The master content of the list
     */
    private List<T> objects;

    /**
     * The top level GUI
     */
    private Updatable master;

    /**
     * @param objects
     * @param master
     */
    public BoundArray(List<T> objects, Updatable master) {
        super();
        this.objects = objects;
        this.master = master;
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void notifyUpdate() {
        master.notifyUpdate();
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
    protected abstract ListSelectionModel getListSelectionModel();

    /**
     * Get the element tired to a index in the list (usual the same but can be different if the display is sorted
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
