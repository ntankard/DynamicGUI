package com.ntankard.DynamicGUI.Unused;

import com.ntankard.DynamicGUI.DataBinding.Bindable;
import com.ntankard.DynamicGUI.Components.BaseSwing.Bound_JComboBox;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Nicholas on 8/06/2016.
 */
public class BoundPrimitivePair_JComboBox<T> extends Bound_JComboBox {

    /**
     * The data source
     */
    private Bindable<T> data;

    /**
     * The possible options (must have toString)
     */
    private ArrayList<Pair<String,T>> options;

    /**
     * Default constructor
     * @param data
     */
    public BoundPrimitivePair_JComboBox(Bindable<T> data, ArrayList<Pair<String,T>> options) {
        this.data = data;
        this.options = options;
        this.setRenderer(new ComboBoxRenderer(this.getRenderer()));
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
        this.removeAllItems();

        // regenerate the list
        options.forEach(this::addItem);

        // find the option
        int index = 0;
        for (Pair<String,T> o:options){
            if(data.get().equals(o.getValue())){
                this.setSelectedIndex(index);
                break;
            }
            index++;
        }

        // sanity check
        if(index == options.size()){
            throw new IllegalStateException("The data isn't in the option");
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void save() {
        data.set(((Pair<String,T>)this.getSelectedItem()).getValue());
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

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Renderer #######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Override the render to access the string
     */
    class ComboBoxRenderer extends JLabel implements ListCellRenderer {

        /**
         * Change if cant be loaded from old version
         */
        private static final long serialVersionUID = 2210915180389193804L;

        /**
         * Default list behavior
         */
        public ListCellRenderer defaultCellRenderer;

        /**
         * Default constructor
         * @param defaultCellRenderer
         */
        public ComboBoxRenderer(ListCellRenderer defaultCellRenderer) {
            this.defaultCellRenderer = defaultCellRenderer;
        }

        /**
         * Drill down to the string
         */
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = defaultCellRenderer.getListCellRendererComponent(list,((Pair<String,T>)value).getKey(),index,isSelected,cellHasFocus);
            return c;
        }
    }
}