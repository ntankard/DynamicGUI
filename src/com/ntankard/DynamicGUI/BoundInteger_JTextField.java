package com.ntankard.DynamicGUI;

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
     * @param data
     */
    public BoundInteger_JTextField(Bindable<Integer> data) {
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
    public boolean validateState(){
        try{
            Integer.parseInt(getText());
            return true;
        }catch (Exception e){
            return false;
        }
    }
}