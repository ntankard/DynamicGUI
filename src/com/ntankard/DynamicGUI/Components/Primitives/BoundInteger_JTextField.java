package com.ntankard.DynamicGUI.Components.Primitives;

import com.ntankard.DynamicGUI.Components.BaseSwing.Bound_JTextField;
import com.ntankard.DynamicGUI.DataBinding.Bindable;

/**
 * Created by Nicholas on 8/06/2016.
 */
public class BoundInteger_JTextField extends Bound_JTextField {

    /**
     * The data source
     */
    private Bindable<Integer> data;

    /**
     * Default constructor
     *
     * @param data
     */
    public BoundInteger_JTextField(Bindable<Integer> data) {
        this.data = data;
        this.setEditable(data.canEdit());
        load();
    }

    //------------------------------------------------------------------------------------------------------------------
    //######################################### Bound_JComponent Implementation ########################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void load() {
        setText(data.get() + "");
    }

    /**
     * @inheritDoc
     */
    @Override
    public void save() {
        data.set(Integer.parseInt(getText()));
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean validateState() {
        try {
            Integer.parseInt(getText());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}