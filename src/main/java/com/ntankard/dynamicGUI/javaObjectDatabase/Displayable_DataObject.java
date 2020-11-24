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

        return dataObjectSchema.endLayer(Displayable_DataObject.class);
    }

    /**
     * @see DataObject#DataObject(Database)
     */
    public Displayable_DataObject(Database database) {
        super(database);
    }

    /**
     * @see DataObject#DataObject(DataObject_Schema)
     */
    public Displayable_DataObject(DataObject_Schema dataObjectSchema) {
        super(dataObjectSchema);
    }
}
