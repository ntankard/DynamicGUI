package com.ntankard.dynamicGUI.gui.containers;

import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database_Schema;
import com.ntankard.javaObjectDatabase.util.set.ObjectSet;

import java.util.ArrayList;

public class DynamicGUI_SetDisplayList <T extends DataObject> extends DynamicGUI_DisplayList<T> {

    /**
     * The source of data for the list
     */
    private final ObjectSet<T> objectSet;

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param master    The parent to notify if data changes
     */
    public DynamicGUI_SetDisplayList(Database_Schema schema, Class<T> tClass, ObjectSet<T> objectSet, Updatable master) {
        this(schema, tClass, objectSet, true, master);
    }

    /**
     * Constructor. Build a list from a objectSet of type tClass. Filter added by default
     *
     * @param tClass    The type of object to display
     * @param objectSet The source of data to display
     * @param filter    Should a filter be added
     * @param master    The parent to notify if data changes
     */
    public DynamicGUI_SetDisplayList(Database_Schema schema, Class<T> tClass, ObjectSet<T> objectSet, boolean filter, Updatable master) {
        super(schema, new ArrayList<>(), tClass, master);
        this.objectSet = objectSet;
        if (filter) {
            addFilter();
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        base.clear();
        base.addAll(objectSet.get());
        super.update();
    }
}
