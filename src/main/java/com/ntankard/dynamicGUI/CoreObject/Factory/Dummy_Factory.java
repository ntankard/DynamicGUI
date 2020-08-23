package com.ntankard.dynamicGUI.CoreObject.Factory;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;

public class Dummy_Factory implements ObjectFactory {

    /**
     * The object that is built
     */
    private final Class<? extends CoreObject> core;

    /**
     * Constructor
     */
    public Dummy_Factory(Class<? extends CoreObject> core) {
        this.core = core;
    }

    /**
     * {@inheritDoc
     */
    @Override
    public Class<? extends CoreObject> getObjectToBuild() {
        return core;
    }
}
