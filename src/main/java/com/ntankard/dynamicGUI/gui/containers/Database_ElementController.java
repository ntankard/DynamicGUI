package com.ntankard.dynamicGUI.gui.containers;

import com.ntankard.dynamicGUI.gui.containers.DynamicGUI_DisplayList.ElementController;
import com.ntankard.dynamicGUI.gui.util.update.Updatable;
import com.ntankard.javaObjectDatabase.dataObject.DataObject;
import com.ntankard.javaObjectDatabase.database.Database;

public abstract class Database_ElementController<T extends DataObject> implements ElementController<T> {

    /**
     * The core database that the element will be entered in
     */
    private final Database database;

    /**
     * The parent to notify when data changes
     */
    private final Updatable master;

    /**
     * Constructor
     */
    public Database_ElementController(Database database, Updatable master) {
        this.database = database;
        this.master = master;
    }

    /**
     * Get the core database that the element will be entered in
     *
     * @return The core database that the element will be entered in
     */
    protected Database getTrackingDatabase() {
        return database;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void deleteElement(T toDel) {
        toDel.remove();
        master.notifyUpdate();
    }

    /**
     * @inheritDoc
     */
    @Override // TODO this needs to be reviewed, is it possible to make an object without adding it
    public void addElement(T newObj) {
        newObj.add();
        master.notifyUpdate();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean canCreate() {
        return true;
    }
}
