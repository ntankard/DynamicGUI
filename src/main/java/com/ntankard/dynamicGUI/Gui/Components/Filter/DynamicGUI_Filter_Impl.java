package com.ntankard.dynamicGUI.Gui.Components.Filter;

import com.ntankard.dynamicGUI.Gui.Components.Filter.Component.*;
import com.ntankard.dynamicGUI.Gui.Util.Containers.PanelContainer;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase_Schema;

import java.util.List;
import java.util.function.Predicate;

public class DynamicGUI_Filter_Impl<T> extends PanelContainer {

    /**
     * The kind of object used to generate this panel
     */
    private final Class<T> aClass;

    /**
     * All the predicates for each of the individual controls
     */
    private final List<Predicate<T>> predicates;

    /**
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private int verbosity;


    /**
     * Constructor
     *
     * @param aClass     The kind of object used to generate this panel
     * @param predicates All the predicates for each of the individual controls
     * @param master     The parent of this object to be notified if data changes
     */
    public DynamicGUI_Filter_Impl(Class<T> aClass, List<Predicate<T>> predicates, Updatable master) {
        super(master);
        this.aClass = aClass;
        this.predicates = predicates;
        this.verbosity = Display_Properties.ALWAYS_DISPLAY;
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

        for (DataField_Schema<?> dataFieldSchema : TrackingDatabase_Schema.getFieldContainer(aClass).getVerbosityDataFields(verbosity)) {

            // find a compatible filter type
            MemberFilter filterable;
            Class<?> theClass = dataFieldSchema.getType();
            if (theClass.equals(String.class)) {
                filterable = new MemberFilter_String(dataFieldSchema, this);
            } else if (theClass.equals(double.class) || dataFieldSchema.getType().equals(Double.class)) {
                filterable = new MemberFilter_Double(dataFieldSchema, this);
            } else if (theClass.isEnum()) {
                filterable = new MemberFilter_Enum(dataFieldSchema, this);
            } else {
                filterable = new MemberFilter_ToString(dataFieldSchema, this);
            }

            addMember(filterable);

            // extra the filter to use
            predicates.add(filterable.getPredicate());
        }
    }
}
