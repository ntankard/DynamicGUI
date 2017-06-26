package com.ntankard.DynamicGUI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.ntankard.GuiUtil.BoundStructure_Generator;

import javafx.util.Pair;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Nicholas on 30/06/2016.
 */
public class PanelManager extends JPanel implements Bound{

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Adders #####################################################
    //------------------------------------------------------------------------------------------------------------------

    // Defaults
    public void add(String name, Bindable toAdd){add(name,toAdd,false);}
    public <T> void add(String name, Bindable data, ArrayList<T> options){add(name,data,options,false);}
    public void add(String name, Bound toAdd){add(name,toAdd,false);}

    /**
     * Add a new row, automatically figures out what kind and binds appropriately
     * @param name
     * @param toAdd
     */
    public void add(String name, Bindable toAdd, Boolean isRestricted){
        if(toAdd == null || toAdd.get() == null){
            return;
        }

        Class toAddType = toAdd.get().getClass();
        if(toAdd.get() instanceof Event){
           // addDataAccess(name,new BoundEvent_JButton(name,toAdd),isRestricted);
            addButton(new BoundEvent_JButton(name,toAdd));
            return;
        }
        if(toAddType == Double.class){
            addDataAccess(name,new BoundDouble_JTextField(toAdd),isRestricted);
            return;
        }
        if(toAddType == Integer.class){
            addDataAccess(name,new BoundInteger_JTextField(toAdd),isRestricted);
            return;
        }
        if(toAddType == String.class){
            addDataAccess(name,new BoundString_JTextField(toAdd),isRestricted);
            return;
        }

        if(toAddType == GregorianCalendar.class){   //@TODO make this more abstract
            //addPanelManager(name,new BoundCalendar_JPanel(toAdd),isRestricted);
            return;
        }
        throw new IllegalStateException("Option not supported");
    }

    /**
     * Add for combo box types
     * @param name
     * @param data
     * @param options
     * @param isRestricted
     */
    public <T> void add(String name, Bindable<T> data, ArrayList<T> options, Boolean isRestricted) {

        if(options == null){
            options = new ArrayList<>();
            options.add(data.get());
        }

        if(options.get(0).getClass() == Pair.class){
            addDataAccess(name,new BoundPrimitivePair_JComboBox(data,options),isRestricted);
            return;
        }else{
            addDataAccess(name,new BoundOptions_JComboBox(data,options),isRestricted);
            return;
        }
    }

    /**
     * Add for panels
     * @param name
     * @param toAdd
     */
    public void add(String name, Bound toAdd,Boolean isRestricted){
        addPanelManager(name,(PanelManager)toAdd,isRestricted);
    }

    /**
     * Add for buttons
     * @param name
     * @param toAdd
     */
    public void add(String name, BoundStructure_Generator toAdd){
       // add(name,toAdd,false);
    }


    /**
     * Add an dynamic panel (one of these)
     * @param name
     * @param toAdd
     * @param isRestricted
     */
    public void addPanelManager(String name,PanelManager toAdd,Boolean isRestricted){
        allRows.add(new PanelRows(name,isRestricted,toAdd));
    }

    /**
     * Add a new standard component
     * @param name
     * @param toAdd
     */
    private void addDataAccess(String name, Bound toAdd, Boolean isRestricted){
        allRows.add(new BoundRows(name,isRestricted,toAdd));
    }

    private void addButton(Bound_JButton toAdd){
        buttonRows.add(toAdd);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Panel Construction ###############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Build the panel
     */
    public void finalizePanel(){
        // create the base panel
        this.removeAll();
        this.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        JPanel toAddP;

        // add general
        if(hadPublic()) {
            toAddP = getGeneralPanel();
            toAddP.setBorder(new TitledBorder("General"));
            lbl_C.setRow(0);
            lbl_C.setRowSpan(2);
            lbl_C.setAnchor(GridConstraints.ANCHOR_NORTH);
            this.add(toAddP, lbl_C);
            lbl_C.setRowSpan(1);
        }

        if(hasPrivate()) {
            toAddP = getRestrictedPanel();
            toAddP.setBorder(new TitledBorder("Restricted"));
            data_C.setRow(0);
            data_C.setAnchor(GridConstraints.ANCHOR_NORTH);
            this.add(toAddP, data_C);
        }

        if(hasButtons()) {
            toAddP = getButtonPanel();
            toAddP.setBorder(new TitledBorder("Events"));
            data_C.setRow(1);
            data_C.setAnchor(GridConstraints.ANCHOR_NORTH);
            this.add(toAddP, data_C);
        }


        /*
        if(hasPrivate() && hasPrivate()){



        } else if(hadPublic()){
            this.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

            // add general
            JPanel toAddP = getGeneralPanel();
            lbl_C.setRow(0);
            this.add(toAddP, lbl_C);
        } else{
            this.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));

            // add general
            JPanel toAddP = getRestrictedPanel();
            toAddP.setBorder(new TitledBorder("General"));
            lbl_C.setRow(0);
            this.add(toAddP, lbl_C);
        }*/
    }

    /**
     * Create a panel with all the general controls
     * @return
     */
    public JPanel getGeneralPanel(){
        JPanel toReturn = new JPanel();
        updateDynamicRows();

        // create the base panel
        toReturn.removeAll();
        toReturn.setLayout(new GridLayoutManager(generalRows.size(), 2, new Insets(0, 0, 0, 0), -1, -1));

        // add all rows
        int insertIndex = 0;
        for(TableRow t: generalRows){
            t.addToPanel(toReturn,insertIndex,false);
            insertIndex++;
        }

        return toReturn;
    }

    /**
     * Create a panel with all the restricted controls
     * @return
     */
    public JPanel getRestrictedPanel(){
        JPanel toReturn = new JPanel();
        updateDynamicRows();

        if(restrictedRows.size() == 0){
            return toReturn;
        }
        // create the base panel
        toReturn.removeAll();
        toReturn.setLayout(new GridLayoutManager(restrictedRows.size(), 2, new Insets(0, 0, 0, 0), -1, -1));

        // add all rows
        int insertIndex = 0;
        for(TableRow t: restrictedRows){
            t.addToPanel(toReturn,insertIndex,true);
            insertIndex++;
        }

        return toReturn;
    }

    /**
     * Create a panel with all the general controls
     * @return
     */
    public JPanel getButtonPanel(){
        JPanel toReturn = new JPanel();

        if(buttonRows.size() == 0){
            return toReturn;
        }

        // create the base panel
        toReturn.removeAll();
        toReturn.setLayout(new GridLayoutManager(buttonRows.size(), 2, new Insets(0, 0, 0, 0), -1, -1));

        // add all rows
        int insertIndex = 0;
        for(Bound_JButton b: buttonRows){
            panel_C.setRow(insertIndex);
            toReturn.add(b,panel_C);
            insertIndex++;
        }

        return toReturn;
    }

    /**
     * Generate the general and restricted rows
     */
    private void updateDynamicRows(){
        generalRows = new ArrayList<>();
        restrictedRows = new ArrayList<>();

        for(TableRow t: allRows){
            if(t instanceof BoundRows){
                if(t.isRestricted){
                    restrictedRows.add(t);
                } else{
                    generalRows.add(t);
                }
            } else{
                if(t.isRestricted){
                    restrictedRows.add(t);
                }else {
                    if (((PanelRows) t).panel.hasPrivate()) {
                        restrictedRows.add(t);
                    }
                    if (((PanelRows) t).panel.hadPublic()) {
                        generalRows.add(t);
                    }
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ General #########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Every row in the system
     */
    private ArrayList<TableRow> allRows = new ArrayList<>();

    /**
     * The general rows to add (dynamically generated)
     */
    private ArrayList<TableRow> generalRows = new ArrayList<>();

    /**
     * The restricted rows to add (dynamically generated)
     */
    private ArrayList<TableRow> restrictedRows = new ArrayList<>();

    private ArrayList<Bound_JButton> buttonRows = new ArrayList<>();

    /**
     * Are there any restricted rows?
     * @return
     */
    private boolean hasPrivate(){
        updateDynamicRows();
        if(restrictedRows.size()==0){
            return false;
        }
        return true;
    }

    /**
     * Are there any public rows?
     * @return
     */
    private boolean hadPublic(){
        updateDynamicRows();
        if(generalRows.size()==0){
            return false;
        }
        return true;
    }

    private boolean hasButtons(){
        if(buttonRows.size()==0){
            return false;
        }
        return true;
    }

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Rows #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public abstract class TableRow {

        /**
         * The name of the row (for a label)
         */
        public String name;

        /**
         * Is the component restricted
         */
        public boolean isRestricted;

        /**
         * Add this row to a standard Grid panel
         * @param toAddTo
         * @param index
         * @param isRestricted
         */
        public abstract void addToPanel(JPanel toAddTo, int index, boolean isRestricted);

        /**
         * Default constructor
         * @param name
         * @param isPrivate
         */
        public TableRow(String name, boolean isPrivate) {
            this.name = name;
            this.isRestricted = isPrivate;
        }
    }

    /**
     * A row containing a label and a JComponent
     */
    public class BoundRows extends TableRow {

        /**
         * The main data component
         */
        public Bound data;

        /**
         * Default constructor
         * @param name
         * @param isPrivate
         * @param data
         */
        public BoundRows(String name, boolean isPrivate, Bound data) {
            super(name, isPrivate);
            this.data = data;
        }

        /**
         * @inheritDoc
         */
        @Override
        public void addToPanel(JPanel toAddTo, int index, boolean isRestricted){
            lbl_C.setRow(index);
            data_C.setRow(index);
            toAddTo.add(new JLabel(name), lbl_C);
            toAddTo.add(data.getComponent(), data_C);
        }
    }

    /**
     * A row containing a PanelManger
     */
    public class PanelRows extends TableRow {

        /**
         * The object to create the panel
         */
        public PanelManager panel;

        /**
         * Default constructor
         * @param name
         * @param isPrivate
         * @param panel
         */
        public PanelRows(String name, boolean isPrivate, PanelManager panel) {
            super(name, isPrivate);
            this.panel = panel;
        }

        /**
         * @inheritDoc
         */
        @Override
        public void addToPanel(JPanel toAddTo, int index, boolean isRestricted){
            panel_C.setRow(index);
            JPanel toAddP;
            if(isRestricted) {
                toAddP = panel.getRestrictedPanel();
            }else{
                toAddP = panel.getGeneralPanel();
            }
            toAddP.setBorder(new TitledBorder(name));
            toAddTo.add(toAddP, panel_C);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Bound Stuff ######################################################
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void load() {}

    @Override
    public void save() {}

    @Override
    public boolean validateState() {
        return true;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void disableAdmin() {}

    @Override
    public void disableAdminSort() {}

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Constants ####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * The constraint for a label in the left column
     */
    private static final GridConstraints lbl_C = new GridConstraints(
            0, 0, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null);

    /**
     * The constraint for a data element in the right column
     */
    private static final GridConstraints data_C =  new GridConstraints(
            0, 1, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null);

    /**
     * The constraint for an object spanning both columns
     */
    private static final GridConstraints panel_C = new GridConstraints(
            0, 0, 1, 2,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null);
}