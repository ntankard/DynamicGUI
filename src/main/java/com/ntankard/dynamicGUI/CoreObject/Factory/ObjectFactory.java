package com.ntankard.dynamicGUI.CoreObject.Factory;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;

public interface ObjectFactory {

    /**
     * Get the object type this factory builds
     *
     * @return The object type this factory builds
     */
    Class<? extends CoreObject> getObjectToBuild();
}
