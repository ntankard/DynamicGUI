package com.ntankard.DynamicGUI.Components.Filter.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Updatable;

import java.lang.reflect.InvocationTargetException;

/**
 * A MemberFilter used to filter an object string value (toString) with either an exact, partial, or case sensitive match
 */
public class MemberFilter_ToString extends MemberFilter_String {

    /**
     * {@inheritDoc}
     */
    public MemberFilter_ToString(Member baseMember, Updatable master) {
        super(baseMember, master);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInstanceValue(Object o) throws InvocationTargetException, IllegalAccessException {
        Object toRead = getBaseMember().getGetter().invoke(o);
        if(toRead != null) {
            return getBaseMember().getGetter().invoke(o).toString();
        }
        return null;
    }
}
