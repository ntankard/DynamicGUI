package com.ntankard.dynamicGUI.Gui.Components.Filter.Component;

import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;

/**
 * A MemberFilter used to filter an object string value (toString) with either an exact, partial, or case sensitive match
 */
public class MemberFilter_ToString extends MemberFilter_String {

    /**
     * Constructor
     *
     * @param dataFieldSchema The DataField that this panel is built around
     * @param master    The top level GUI
     */
    public MemberFilter_ToString(DataField_Schema<?> dataFieldSchema, Updatable master) {
        super(dataFieldSchema, master);
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getInstanceValue(DataObject o) {
        Object toRead = o.get(getDataFieldSchema().getIdentifierName());
        if (toRead != null) {
            return toRead.toString();
        }
        return null;
    }
}
