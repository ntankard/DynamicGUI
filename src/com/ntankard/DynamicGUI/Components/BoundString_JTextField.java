package com.ntankard.DynamicGUI.Components;

import com.ntankard.DynamicGUI.Bindable;
import com.ntankard.DynamicGUI.Components.Bound_JTextField;

/**
 * Created by Nicholas on 8/06/2016.
 */
public class BoundString_JTextField extends Bound_JTextField {

    /**
     * The data source
     */
    private Bindable<String> data;

    /**
     * Default constructor
     * @param data
     */
    public BoundString_JTextField(Bindable<String> data) {
        this.data = data;
        this.setEditable(data.canEdit());
        load();
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Bound_JComponent Implementation #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void load() {
        setText((data.get()));
    }

    /**
     * @inheritDoc
     */
    @Override
    public void save() {
        data.set(getText());
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean validateState(){
        return true;
    }
}