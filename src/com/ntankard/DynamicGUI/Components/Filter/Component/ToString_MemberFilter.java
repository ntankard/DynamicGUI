package com.ntankard.DynamicGUI.Components.Filter.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Util.Updatable;

import java.lang.reflect.InvocationTargetException;

public class ToString_MemberFilter extends String_MemberFilter {
    /**
     * {@inheritDoc}
     *
     * @param baseMember
     * @param master
     */
    public ToString_MemberFilter(Member baseMember, Updatable master) {
        super(baseMember, master);
    }

    protected String getInstanceValue(Object o) throws InvocationTargetException, IllegalAccessException {
        Object toRead = baseMember.getGetter().invoke(o);
        if(toRead != null) {
            return (String) baseMember.getGetter().invoke(o).toString();
        }
        return null;
    }
}
