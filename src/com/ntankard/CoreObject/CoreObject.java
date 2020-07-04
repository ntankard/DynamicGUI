package com.ntankard.CoreObject;

import com.ntankard.CoreObject.Field.DataField;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class CoreObject {

    //------------------------------------------------------------------------------------------------------------------
    //################################################### Field Setup ##################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * The method name to get the fields
     */
    public static String FieldName = "getFieldContainer";

    /**
     * Get all the fields for this object
     */
    public static FieldContainer getFieldContainer() {
        FieldContainer fieldContainer = new FieldContainer();

        return fieldContainer.endLayer(CoreObject.class);
    }

    /**
     * Get all the fields for this object an object
     *
     * @param aClass The object to get
     */
    public static FieldContainer getFieldContainer(Class<?> aClass) {
        try {
            Method method = aClass.getDeclaredMethod(CoreObject.FieldName);
            return ((FieldContainer) method.invoke(null));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ee) {
            throw new RuntimeException("Cant extract object fields", ee);
        }
    }

    /**
     * The fields for this DataObject
     */
    protected Map<String, DataField<?>> fieldMap = new HashMap<>();

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Field Interface #################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get a specific field
     *
     * @param field The Field to get
     * @param <T>   THe type of the field
     * @return The field
     */
    @SuppressWarnings("unchecked")
    public <T> DataField<T> getField(String field) {
        return (DataField<T>) fieldMap.get(field);
    }

    /**
     * Set the value from a specific field
     *
     * @param field The field to set
     * @param value THe value to set
     * @param <T>   The type of the Field
     */
    @SuppressWarnings("unchecked")
    public <T> void set(String field, T value) {
        ((DataField<T>) fieldMap.get(field)).set(value);
    }

    /**
     * Get the value from a specific field
     *
     * @param field The Field to get
     * @param <T>   The type of the Field
     * @return The value of the field
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String field) {
        return (T) getField(field).get();
    }
}
