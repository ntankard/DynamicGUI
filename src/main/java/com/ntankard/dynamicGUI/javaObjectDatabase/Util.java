package com.ntankard.dynamicGUI.javaObjectDatabase;

import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.javaObjectDatabase.dataObject.DataObject_Schema;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static List<DataField_Schema<?>> getVerbosityDataFields(DataObject_Schema dataObject_schema, int verbosity) {
        List<DataField_Schema<?>> fields = new ArrayList<>();
        for (DataField_Schema<?> f : dataObject_schema.getList()) {
            if (f.getDisplayProperties().getVerbosityLevel() > verbosity) {
                continue;
            }
            if (!f.getDisplayProperties().getShouldDisplay()) {
                continue;
            }
            fields.add(f);
        }
        return fields;
    }
}
