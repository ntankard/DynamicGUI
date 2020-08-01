package com.ntankard.dynamicGUI.Gui.Containers;

import com.ntankard.dynamicGUI.Gui.Components.Filter.DynamicGUI_Filter_Impl;
import com.ntankard.dynamicGUI.Gui.Util.Containers.ControllablePanel;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;

import java.util.List;
import java.util.function.Predicate;

public class DynamicGUI_Filter<T> extends ControllablePanel<DynamicGUI_Filter_Impl<T>, ControllablePanel> {

    /**
     * Constructor
     *
     * @param master The parent of this object to be notified if data changes
     */
    public DynamicGUI_Filter(Class<T> aClass, List<Predicate<T>> predicates, Updatable master) {
        super(master);
        setMainPanel(new DynamicGUI_Filter_Impl<>(aClass, predicates, master));
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
