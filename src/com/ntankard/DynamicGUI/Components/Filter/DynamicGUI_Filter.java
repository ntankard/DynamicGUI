package com.ntankard.DynamicGUI.Components.Filter;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Util.Swing.Containers.ControllablePanel;
import com.ntankard.DynamicGUI.Util.Updatable;

import java.util.List;
import java.util.function.Predicate;

public class DynamicGUI_Filter extends ControllablePanel<DynamicGUI_Filter_Impl, ControllablePanel> {

    //------------------------------------------------------------------------------------------------------------------
    //############################################ Master Factories ####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Create a new DynamicGUI_Filter_Impl panel
     *
     * @param mClass     The kind of object used to generate this panel
     * @param predicates All the predicates for each of the individual controls
     * @param verbosity  What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param master     The parent of this object to be notified if data changes
     */
    public static <T> DynamicGUI_Filter newFilterPanel(MemberClass mClass, List<Predicate<T>> predicates, int verbosity, Updatable master) {
        DynamicGUI_Filter container = new DynamicGUI_Filter(master);

        DynamicGUI_Filter_Impl<T> main = new DynamicGUI_Filter_Impl<>(mClass, predicates, verbosity, container);
        container.setMainPanel(main);

        return container;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################### Core Object ######################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    private DynamicGUI_Filter(Updatable master) {
        super(master);
    }
}
