package com.ntankard.dynamicGUI.Gui.Components.Filter.Component;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;

/**
 * A MemberFilter used to filter an object string value (toString) with either an exact, partial, or case sensitive match
 */
public class MemberFilter_ToString extends MemberFilter_String {

    /**
     * Constructor
     *
     * @param dataField The DataField that this panel is built around
     * @param master    The top level GUI
     */
    public MemberFilter_ToString(DataField<?> dataField, Updatable master) {
        super(dataField, master);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInstanceValue(CoreObject o) {
        Object toRead = o.get(getDataField().getIdentifierName());
        if (toRead != null) {
            return toRead.toString();
        }
        return null;
    }
}
