package com.ntankard.DynamicGUI.Components;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.ntankard.DynamicGUI.Unused.Bound_JButton;

import com.ntankard.DynamicGUI.Generator.Rows.BoundRows;
import com.ntankard.DynamicGUI.Generator.Rows.PanelRows;
import com.ntankard.DynamicGUI.Generator.Rows.TableRow;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

import static com.ntankard.DynamicGUI.Generator.Rows.BoundRows.data_C;
import static com.ntankard.DynamicGUI.Generator.Rows.BoundRows.lbl_C;
import static com.ntankard.DynamicGUI.Generator.Rows.PanelRows.panel_C;

/**
 * Created by Nicholas on 30/06/2016.
 */
public class BoundComposite_JPanel extends JPanel implements Bound_JComponent {

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Adders #####################################################
    //------------------------------------------------------------------------------------------------------------------


    /**
     * Add an dynamic panel (one of these)
     * @param name
     * @param toAdd
     * @param isRestricted
     */
    public void addPanelManager(String name, BoundComposite_JPanel toAdd, Boolean isRestricted){
        allRows.add(new PanelRows(name,isRestricted,toAdd));
    }

    /**
     * Add a new standard component
     * @param name
     * @param toAdd
     */
    public void addDataAccess(String name, Bound_JComponent toAdd, Boolean isRestricted){
        allRows.add(new BoundRows(name,isRestricted,toAdd));
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

        // add restricted
        if(hasPrivate()) {
            toAddP = getRestrictedPanel();
            toAddP.setBorder(new TitledBorder("Restricted"));
            data_C.setRow(0);
            data_C.setAnchor(GridConstraints.ANCHOR_NORTH);
            this.add(toAddP, data_C);
        }
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


    //------------------------------------------------------------------------------------------------------------------
    //############################################# Bound_JComponent Stuff #############################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public void load() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void save() {
        for(TableRow row : allRows){
            if(row instanceof BoundRows){
                BoundRows bound = (BoundRows)row;
                bound.data.save();
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateState() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JComponent getComponent() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableAdmin() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableAdminSort() {}

    //------------------------------------------------------------------------------------------------------------------
    //##################################################### Unused #####################################################
    //------------------------------------------------------------------------------------------------------------------

    @Deprecated
    private ArrayList<Bound_JButton> buttonRows = new ArrayList<>();

    @Deprecated
    private void addButton(Bound_JButton toAdd){
        buttonRows.add(toAdd);
    }

    @Deprecated
    private boolean hasButtons(){
        if(buttonRows.size()==0){
            return false;
        }
        return true;
    }

    /**
     * Create a panel with all the general controls
     * @return
     */
    @Deprecated
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

    /*public void finalizePanel(){
        if(hasButtons()) {
            toAddP = getButtonPanel();
            toAddP.setBorder(new TitledBorder("Events"));
            data_C.setRow(1);
            data_C.setAnchor(GridConstraints.ANCHOR_NORTH);
            this.add(toAddP, data_C);
        }
    }*/
}