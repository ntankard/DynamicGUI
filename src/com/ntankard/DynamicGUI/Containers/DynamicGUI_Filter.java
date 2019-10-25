package com.ntankard.DynamicGUI.Containers;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.Filter.DynamicGUI_Filter_Impl;
import com.ntankard.DynamicGUI.Util.Containers.ControllablePanel;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import java.util.List;
import java.util.function.Predicate;

public class DynamicGUI_Filter<T> extends ControllablePanel<DynamicGUI_Filter_Impl, ControllablePanel> {

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public DynamicGUI_Filter(MemberClass mClass, List<Predicate<T>> predicates, Updatable master) {
        super(master);
        setMainPanel(new DynamicGUI_Filter_Impl<>(mClass, predicates, master));
    }

    /**
     * Set what level of verbosity should be shown?
     *
     * @param verbosity What level of verbosity should be shown?
     * @return This
     */
    public DynamicGUI_Filter<T> setVerbosity(int verbosity) {
        getMainPanel().setVerbosity(verbosity);
        return this;
    }
}
