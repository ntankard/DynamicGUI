package com.ntankard.dynamicGUI.javaObjectDatabase;

import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;
import com.ntankard.javaObjectDatabase.database.Database;

public abstract class Displayable_DataObject extends DataObject {

    /**
     * Get all the fields for this object
     */
    public static DataObject_Schema getDataObjectSchema() {
        DataObject_Schema dataObjectSchema = DataObject.getDataObjectSchema();

        // Add display properties
        dataObjectSchema.addPropertyBuilder(dataFieldSchema -> dataFieldSchema.setProperty(new Display_Properties()));
        dataObjectSchema.get(DataObject_Id).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.INFO_DISPLAY);
        dataObjectSchema.get(DataObject_ChildrenField).getProperty(Display_Properties.class).setVerbosityLevel(Display_Properties.TRACE_DISPLAY);

        return dataObjectSchema.endLayer(Displayable_DataObject.class);
    }

    /**
     * @see DataObject#DataObject(Database, Object...)
     */
    public Displayable_DataObject(Database database, Object... args) {
        super(database, args);
    }
}
