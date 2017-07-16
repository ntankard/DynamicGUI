package com.ntankard.DynamicGUI.Components.Primitives;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.ntankard.DynamicGUI.DataBinding.Bindable;
import com.ntankard.DynamicGUI.Components.BaseSwing.Bound_JPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Nicholas on 8/06/2016.
 */
public class BoundCalendar_JPanel extends Bound_JPanel {

    //------------------------------------------------------------------------------------------------------------------
    //######################################### Bound_JComponent Implementation ########################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    public void load() {
        year_comboBox.setSelectedIndex(data.get().get(Calendar.YEAR) - STARTING_YEAR);
        month_comboBox.setSelectedIndex(data.get().get(Calendar.MONTH));
        generateDay();
        day_comboBox.setSelectedIndex(data.get().get(Calendar.DAY_OF_MONTH)-1);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void save() {
        data.get().set(Calendar.YEAR,year_comboBox.getSelectedIndex() + STARTING_YEAR);
        data.get().set(Calendar.MONTH,month_comboBox.getSelectedIndex());
        data.get().set(Calendar.DAY_OF_MONTH,day_comboBox.getSelectedIndex()+1);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean validateState(){
        return true;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * The earliest date possible
     */
    private static final int STARTING_YEAR = 2014;

    /**
     * Generate the days of the month based on the months and year
     */
    private void generateDay() {
        int selected = day_comboBox.getSelectedIndex();
        Calendar ref = new GregorianCalendar(year_comboBox.getSelectedIndex()+ STARTING_YEAR, month_comboBox.getSelectedIndex(), 1);
        int days = ref.getActualMaximum(Calendar.DAY_OF_MONTH);

        day_comboBox.removeAllItems();
        for(int i=0;i<days;i++) {
            day_comboBox.addItem(i+1);
        }

        if(selected < days) {
            day_comboBox.setSelectedIndex(selected);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Constructor ######################################################
    //------------------------------------------------------------------------------------------------------------------

    public BoundCalendar_JPanel(Bindable<Calendar> data) {
        this.data = data;
        createUIComponents();
        this.setEnabled(data.canEdit());
        load();
    }

    /**
     * Construct the panel
     */
    private void createUIComponents() {
        year_comboBox = new JComboBox();
        for(int i=0;i<30;i++) {
            year_comboBox.addItem((STARTING_YEAR + i));
        }
        year_comboBox.addActionListener (e -> generateDay());
        GridConstraints year_comboBox_C = new GridConstraints(0, 2, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null);

        month_comboBox = new JComboBox();
        month_comboBox.addItem("JAN");
        month_comboBox.addItem("FEB");
        month_comboBox.addItem("MAR");
        month_comboBox.addItem("APR");
        month_comboBox.addItem("MAY");
        month_comboBox.addItem("JUN");
        month_comboBox.addItem("JUL");
        month_comboBox.addItem("AUG");
        month_comboBox.addItem("SEP");
        month_comboBox.addItem("OCT");
        month_comboBox.addItem("NOV");
        month_comboBox.addItem("DEC");
        month_comboBox.setSelectedIndex(0);
        month_comboBox.addActionListener (e -> generateDay());
        GridConstraints month_comboBox_C = new GridConstraints(
                0, 1, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null);

        day_comboBox = new JComboBox();
        generateDay();
        GridConstraints day_comboBox_C = new GridConstraints(0, 0, 1, 1,
                GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
                GridConstraints.SIZEPOLICY_CAN_GROW,
                GridConstraints.SIZEPOLICY_FIXED,
                null, null, null);

        this.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        this.add(day_comboBox, day_comboBox_C);
        this.add(year_comboBox, year_comboBox_C);
        this.add(month_comboBox, month_comboBox_C);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### GUI Objects ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * The data source
     */
    private Bindable<Calendar> data;

    private JComboBox day_comboBox;
    private JComboBox year_comboBox;
    private JComboBox month_comboBox;
}