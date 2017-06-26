package com.ntankard.DynamicGUI;

import java.util.ArrayList;

/**
 * Created by Nicholas on 8/06/2016.
 */
public class BoundOptions_JComboBox<T> extends Bound_JComboBox {

    /**
     * The data source
     */
    private Bindable<T> data;

    /**
     * The possible options (must have toString)
     */
    private ArrayList<T> options;

    /**
     * Default constructor
     * @param data
     */
    public BoundOptions_JComboBox(Bindable<T> data, ArrayList<T> options) {
        this.data = data;
        this.options = options;
        load();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Bound Implementation #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void load() {
        this.removeAllItems();

        // regenerate the list
        options.forEach(this::addItem);

        // if null, have no selection
        if(data.get() == null){
            this.setSelectedIndex(-1);
            return;
        }

        // find the option
        int index = 0;
        for (T o:options){
            if(o == data.get()){
                this.setSelectedIndex(index);
                return;
            }
            index++;
        }

        throw new IllegalStateException("The data isn't in the option");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void save() {
        data.set(this.getSelectedItem());
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean validateState(){
        if(getSelectedIndex() < 0 || getSelectedIndex() > options.size()){
            return false;
        }
        return true;
    }
}
