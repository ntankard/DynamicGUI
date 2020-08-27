package com.ntankard.dynamicGUI.CoreObject.Factory;

import com.ntankard.dynamicGUI.CoreObject.CoreObject;

public class Dummy_Factory<GeneratedType extends CoreObject, GeneratorType extends CoreObject> extends ObjectFactory<GeneratedType, GeneratorType> {

    /**
     * Constructor
     */
    public Dummy_Factory(Class<GeneratedType> buildTypeClass) {
        super(buildTypeClass);
    }

    /**
     * {@inheritDoc
     */
    @Override
    public void generate(GeneratorType source) {
        throw new UnsupportedOperationException("A Dummy factory can no make objects. This must be done manually in the add method");
    }
}
