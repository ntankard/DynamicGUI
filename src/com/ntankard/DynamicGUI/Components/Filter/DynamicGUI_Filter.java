package com.ntankard.DynamicGUI.Components.Filter;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.Base.Filter.Component.*;
import com.ntankard.DynamicGUI.Components.Filter.Component.*;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Predicate;

public class DynamicGUI_Filter extends Updatable.UpdatableJScrollPane {

    /**
     * The kind of object used to generate this panel
     */
    private MemberClass mClass;

    /**
     * All the predicates for each of the individual controls
     */
    private List<Predicate> predicates;

    /**
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private int verbosity;

    /**
     * Constructor
     * @param mClass
     * @param predicates
     * @param verbosity
     * @param master
     */
    public DynamicGUI_Filter(MemberClass mClass, List<Predicate> predicates, int verbosity,Updatable master) {
        super(master);
        this.mClass = mClass;
        this.predicates = predicates;
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
        for(Member member : mClass.getVerbosityMembers(verbosity)){

            // find a compatible filter type
            MemberFilter filterable;
            Class<?> theClass = member.getType();
            if(theClass.equals(String.class)){
                filterable = new MemberFilter_String(member,this);
            }else if(theClass.equals(double.class) || member.getType().equals(Double.class)){
                filterable = new MemberFilter_Double(member,this);
            }else if(theClass.isEnum()){
                filterable = new MemberFilter_Enum(member,this);
            }else{
                filterable = new MemberFilter_ToString(member,this);
            }

            // add the new filter component
            labelC.gridy = index;
            componentC.gridy = index;
            panel.add(filterable,componentC);
            index++;

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
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    public void update() {
    }
}