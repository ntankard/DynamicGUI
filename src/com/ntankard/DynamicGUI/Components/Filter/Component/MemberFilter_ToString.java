package com.ntankard.DynamicGUI.Components.Filter.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import java.lang.reflect.InvocationTargetException;

/**
 * A MemberFilter used to filter an object string value (toString) with either an exact, partial, or case sensitive match
 */
public class MemberFilter_ToString extends MemberFilter_String {

    /**
     * Constructor
     *
     * @param baseMember The member connected to this panel
     * @param master     The top level GUI
     */
    public MemberFilter_ToString(Member baseMember, Updatable master) {
        super(baseMember, master);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInstanceValue(Object o) throws InvocationTargetException, IllegalAccessException {
        Object toRead = getBaseMember().getGetter().invoke(o);
        if (toRead != null) {
            return getBaseMember().getGetter().invoke(o).toString();
        }
        return null;
    }
}
