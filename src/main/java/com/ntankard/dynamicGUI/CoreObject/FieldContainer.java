package com.ntankard.dynamicGUI.CoreObject;

import com.ntankard.dynamicGUI.CoreObject.Factory.ObjectFactory;
import com.ntankard.dynamicGUI.CoreObject.Field.DataField;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldContainer {

    // Field -----------------------------------------------------------------------------------------------------------

    /**
     * The master container for the fields
     */
    final Map<String, DataField<?>> masterMap = new HashMap<>();

    /**
     * A list stored look up optimization
     */
    private final List<DataField<?>> list = new ArrayList<>();

    /**
     * The last added field
     */
    private DataField<?> last = null;

    /**
     * The last order value set
     */
    private int lastOrder = 0;

    /**
     * The current amount to increment while incrementing the order (must be a multiple of 10)
     */
    private int orderStep = 10000000;

    // Class Behavior --------------------------------------------------------------------------------------------------

    /**
     * All the factory this class has
     */
    private final List<ObjectFactory<?, ?>> objectFactories = new ArrayList<>();

    // State -----------------------------------------------------------------------------------------------------------

    /**
     * Has this container been finalised
     */
    private boolean isFinalized = false;

    /**
     * All the layers used to build the final object. Stored for testing purposes
     */
    final List<Class<? extends CoreObject>> inheritedObjects = new ArrayList<>();

    /**
     * The lowest level of object that has added to this container. Stored for testing purposes
     */
    Class<? extends CoreObject> solidObjectType = null;

    //------------------------------------------------------------------------------------------------------------------
    //#################################################### Field IO ####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Add a field after the key provided
     *
     * @param toFollowKey The key for the field to follow
     * @param dataField   The field to add
     * @return The field that was just added
     */
    public DataField<?> add(String toFollowKey, DataField<?> dataField) {
        if (isFinalized)
            throw new IllegalStateException("Trying to modify a finalised container");
        lastOrder = masterMap.get(toFollowKey).getDisplayProperties().getOrder();
        return add(dataField);
    }

    /**
     * Add a new field
     *
     * @param dataField The field to add
     * @return The field that was just added
     */
    public DataField<?> add(DataField<?> dataField) {
        if (isFinalized)
            throw new IllegalStateException("Trying to modify a finalised container");
        if (masterMap.containsKey(dataField.getIdentifierName()))
            throw new IllegalArgumentException("A field with this key has been added before");


        list.add(dataField);
        masterMap.put(dataField.getIdentifierName(), dataField);
        last = dataField;

        lastOrder = lastOrder + orderStep;
        dataField.getDisplayProperties().setOrder(lastOrder);

        return dataField;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Class Behavior #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Add a new object factory to the list
     *
     * @param objectFactory The object factory to add
     */
    public void addObjectFactory(ObjectFactory objectFactory) {
        if (isFinalized)
            throw new IllegalStateException("Trying to modify a finalised container");
        this.objectFactories.add(objectFactory);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Finalization ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * End this layer of the group. Order will increment by 10 times less after this and will follow the filed toFollowKey
     *
     * @param endLayer    The Object for the current layer
     * @param toFollowKey The key of the field for all future orders to follow
     * @return This
     */
    public FieldContainer endLayer(Class<? extends CoreObject> endLayer, String toFollowKey) {
        if (isFinalized)
            throw new IllegalStateException("Trying to modify a finalised container");
        lastOrder = masterMap.get(toFollowKey).getDisplayProperties().getOrder();
        return endLayer(endLayer);
    }

    /**
     * End this layer of the group. Order will increment by 10 times less after this and will follow the last added field
     *
     * @param endLayer The Object for the current layer
     * @return This
     */
    public FieldContainer endLayer(Class<? extends CoreObject> endLayer) {
        if (isFinalized)
            throw new IllegalStateException("Trying to modify a finalised container");
        if (!Modifier.isAbstract(endLayer.getModifiers()))
            throw new IllegalStateException("Trying to end a layer on a solid object, the last object in the chain should call endContainer(...) instead");
        if (inheritedObjects.contains(endLayer))
            throw new IllegalArgumentException("Trying end a layer twice");
        if (orderStep <= 10)
            throw new IllegalStateException("To many layers added, can no longer decrement the order");

        inheritedObjects.add(endLayer);
        orderStep = orderStep / 10;

        return this;
    }

    /**
     * The the full container, no fields can be added after this. The container will be validated and the fields will be finalized
     *
     * @param end The final solid class containing all the fields
     * @return This
     */
    @SuppressWarnings("unchecked")
    public FieldContainer finaliseContainer(Class<? extends CoreObject> end) {
        if (isFinalized)
            throw new IllegalStateException("Trying to modify a finalised container");
        if (Modifier.isAbstract(end.getModifiers()))
            throw new IllegalStateException("Trying to end the container on an abstract object");
        if (inheritedObjects.contains(end))
            throw new IllegalArgumentException("Trying end a layer twice");

        inheritedObjects.add(end);

        // Test that the tree was constructed correctly
        Class<? extends CoreObject> aClass = end;
        do {
            if (inheritedObjects.size() == 0)
                throw new IllegalStateException("Not every layer invoked endLayer(...)");
            Class<? extends CoreObject> toTest = inheritedObjects.get(inheritedObjects.size() - 1);

            if (!toTest.equals(aClass)) {
                throw new IllegalStateException("A layer has been missed. Solid class:" + end.getSimpleName() + " Expected class:" + toTest.getSimpleName() + " Actual class:" + aClass.getSimpleName());
            }

            // Jump up the inheritance tree
            aClass = (Class<? extends CoreObject>) aClass.getSuperclass();
            inheritedObjects.remove(toTest);
        } while (CoreObject.class.isAssignableFrom(aClass));

        // Finalise all fields
        for (DataField<?> dataField : list) {
            dataField.containerFinished(end);
        }

        solidObjectType = end;
        isFinalized = true;
        return this;
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Field Access ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get a field from its key
     *
     * @param key The key to get
     * @param <T> The type of the field
     * @return The requested field
     */
    @SuppressWarnings("unchecked")
    public <T> DataField<T> get(String key) {
        return (DataField<T>) masterMap.get(key);
    }

    /**
     * Get the list of all fields
     *
     * @return The list of fields
     */
    public List<DataField<?>> getList() {
        return list;
    }

    /**
     * Get the list of all fields with a certain, or above verbosity level
     *
     * @param verbosity The level to filter on
     * @return The list of fields
     */
    public List<DataField<?>> getVerbosityDataFields(int verbosity) {
        List<DataField<?>> fields = new ArrayList<>();
        for (DataField<?> f : getList()) {
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

    /**
     * Get the last field that was added
     *
     * @param <T> The type of the field
     * @return The last field that was added
     */
    @SuppressWarnings("unchecked")
    public <T> DataField<T> getLast() {
        return (DataField<T>) last;
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Class Behavior Access ##############################################
    //------------------------------------------------------------------------------------------------------------------

    public List<ObjectFactory<?, ?>> getObjectFactories() {
        return objectFactories;
    }
}
