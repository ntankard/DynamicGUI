package com.ntankard.DynamicGUI.Components.Filter;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.Filter.Component.*;
import com.ntankard.DynamicGUI.Components.List.BoundArray_Properties;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ArrayFilter_JPanel<T> extends JScrollPane implements Updatable {

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
    private ArrayList<Predicate> predicates = new ArrayList<>();

    /**
     * What level of verbosity should be shown? (compared against BoundArray_Properties verbosity)
     */
    private int verbosity;

    /**
     *
     * @param base
     * @param filtered
     * @param master
     */
    public ArrayFilter_JPanel(ArrayList<T> base, ArrayList<T> filtered, Updatable master, MemberClass mClass,int verbosity) {
        this.base = base;
        this.filtered = filtered;
        this.master = master;
        this.mClass = mClass;
        this.verbosity = verbosity;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints labelC = new GridBagConstraints();
        GridBagConstraints componentC = new GridBagConstraints();
        componentC.weightx = 1;
        componentC.fill = GridBagConstraints.BOTH;

        int index = 0;
        for(Member member : mClass.getMembers()){
            BoundArray_Properties properties = member.getGetter().getAnnotation(BoundArray_Properties.class);
            if (properties != null) {
                // should we skip this cell? or dig deeper into it
                if (properties.verbosityLevel() > verbosity) {
                    continue;
                }
            }

            // find a compatible filter type
            MemberFilter_JPanel filterable;
            Class<?> theClass = member.getType();
            if(theClass.equals(String.class)){
                filterable = new String_MemberFilter(member,this);
            }else if(theClass.equals(double.class) || member.getType().equals(Double.class)){
                filterable = new Double_MemberFilter(member,this);
            }else if(theClass.isEnum()){
                filterable = new Enum_MemberFilter(member,this);
            }else{
                filterable = new ToString_MemberFilter(member,this);
            }

            // add the new filter component
            labelC.gridy = index;
            componentC.gridy = index;
            index++;
           // panel.add(new JLabel(member.getName()),labelC);
            panel.add(filterable,componentC);

            // extra the filter to use
            predicates.add(filterable.getPredicate());
        }

        // add final spacer to ensure the content stays at the top of the panel
        GridBagConstraints spacerC = new GridBagConstraints();
        spacerC.weighty = 1;
        spacerC.gridy = index;
        panel.add(new JSeparator(),spacerC);

        this.setViewportView(panel);
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyUpdate() {
        master.notifyUpdate();
    }
}