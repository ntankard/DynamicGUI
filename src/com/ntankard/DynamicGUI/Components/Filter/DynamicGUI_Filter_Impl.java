package com.ntankard.DynamicGUI.Components.Filter;

import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.Filter.Component.*;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayTable_Impl;
import com.ntankard.DynamicGUI.Util.Containers.PanelContainer;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import java.util.List;
import java.util.function.Predicate;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class DynamicGUI_Filter_Impl<T> extends PanelContainer {

    /**
     * The kind of object used to generate this panel
     */
    private MemberClass mClass;

    /**
     * All the predicates for each of the individual controls
     */
    private List<Predicate<T>> predicates;

    /**
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private int verbosity;

    /**
     * Constructor
     *
     * @param mClass     The kind of object used to generate this panel
     * @param predicates All the predicates for each of the individual controls
     * @param master     The parent of this object to be notified if data changes
     */
    public DynamicGUI_Filter_Impl(MemberClass mClass, List<Predicate<T>> predicates, Updatable master) {
        super(master);
        this.mClass = mClass;
        this.predicates = predicates;
        this.verbosity = ALWAYS_DISPLAY;
        createUIComponents();
        update();
    }

    /**
     * Set what level of verbosity should be shown?
     *
     * @param verbosity What level of verbosity should be shown?
     * @return This
     */
    public DynamicGUI_Filter_Impl<T> setVerbosity(int verbosity) {
        this.verbosity = verbosity;
        createUIComponents();
        update();
        return this;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    protected void createUIComponents() {
        super.createUIComponents();

        for (Member member : mClass.getVerbosityMembers(verbosity)) {

            // find a compatible filter type
            MemberFilter filterable;
            Class<?> theClass = member.getType();
            if (theClass.equals(String.class)) {
                filterable = new MemberFilter_String(member, this);
            } else if (theClass.equals(double.class) || member.getType().equals(Double.class)) {
                filterable = new MemberFilter_Double(member, this);
            } else if (theClass.isEnum()) {
                filterable = new MemberFilter_Enum(member, this);
            } else {
                filterable = new MemberFilter_ToString(member, this);
            }

            addMember(filterable);

            // extra the filter to use
            predicates.add(filterable.getPredicate());
        }
    }
}
