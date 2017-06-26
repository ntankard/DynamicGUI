package com.ntankard.DynamicGUI;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Nicholas on 8/06/2016.
 */
public class BoundDouble_JTextField extends Bound_JTextField {

    /**
     * The data source
     */
    private Bindable<Double> data;

    /**
     * Default constructor
     * @param data
     */
    public BoundDouble_JTextField(Bindable<Double> data) {
        this.data = data;
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
        NumberFormat formatter = new DecimalFormat("#0.00");
        setText(formatter.format(data.get()));
    }

    /**
     * @inheritDoc
     */
    @Override
    public void save() {
        data.set(Double.parseDouble(getText()));
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean validateState(){
        try{
            Double.parseDouble(getText());
            return true;
        }catch (Exception e){
            return false;
        }
    }
}