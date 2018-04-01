package com.ntankard.DynamicGUI.Composite;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.Base.Filter.DynamicGUI_Filter;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DynamicGUI_FilteredList<T> extends JPanel implements Updatable {

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Factories #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public static <T> DynamicGUI_FilteredList newStandardFilterdList(ArrayList<T> base, Updatable master, MemberClass mClass, int verbosity) {
        return new DynamicGUI_FilteredList(base,master,mClass,verbosity);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Core ##########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * The master content of the list
     */
    private ArrayList<T> base;

    /**
     * The version of the list with the fillers applied
     */
    private ArrayList<T> filtered;

    /**
     * The top level GUI
     */
    protected Updatable master;

    /**
     * The kind of object used to generate this panel
     */
    private MemberClass mClass;

    /**
     * All the predicates for each of the individual controls
     */
    private ArrayList<Predicate> predicates;

    /**
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private int verbosity;

    /**
     *
     * @param base
     * @param master
     */
    private DynamicGUI_FilteredList(ArrayList<T> base, Updatable master, MemberClass mClass, int verbosity) {
        this.base = base;
        this.filtered = new ArrayList<>();
        this.predicates = new ArrayList<>();

        this.master = master;
        this.mClass = mClass;
        this.verbosity = verbosity;
        createUIComponents();
        update();
    }

   // private JPanel record_panel;
    private JPanel recordTable_panel;
    private DynamicGUI_Filter recordFilter_panel;

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
       // recordFilter_panel = DynamicGUI_Filter.newStandardDisplayList( this,mClass,predicates,verbosity);
        recordFilter_panel = new DynamicGUI_Filter(mClass,predicates,verbosity,this);
        recordTable_panel = DynamicGUI_EditableList.newStandardDisplayTable(filtered, 0,this);
        this.setLayout(new GridBagLayout());
        //record_panel = new JPanel(new GridBagLayout());

        GridBagConstraints recordTable_c = new GridBagConstraints();
        recordTable_c.weightx = 1;
        recordTable_c.weighty = 1;
        recordTable_c.fill = GridBagConstraints.BOTH;
        this.add(recordTable_panel, recordTable_c);

        GridBagConstraints recordFilter_c = new GridBagConstraints();
        recordFilter_c.fill = GridBagConstraints.BOTH;
        this.add(recordFilter_panel,recordFilter_c);

        update();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
        filtered.clear();

        if(base.size() != 0) {
            List filteredList = base.stream().filter((T o) -> {
                for(Predicate p : predicates){
                    if(!p.test(o)){
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());
            filtered.addAll(filteredList);
        }else{
            filtered.addAll(base);
        }

        ((Updatable)recordTable_panel).update();
       // recordTable_panel.update();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyUpdate() {
        master.notifyUpdate();
    }
}