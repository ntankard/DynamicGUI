package com.ntankard.DynamicGUI.Generator;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * A replacement for Class that includes a few layers higher in the hierarchy (based on ClassGuiProperties)
 */
public class ExtendedClass {

    /** The lowest class */
    private Class<?> baseType;

    /** The classes baseType extends that should be used as part of Gui generation */
    private ArrayList<Class<?>> inheritedClasses = new ArrayList<>();

    /**
     * Default constructor
     * @param
     */
    public ExtendedClass(Object o){
        baseType = o.getClass();
        inheritedClasses.add(baseType);

        while(true) {
            Class child = inheritedClasses.get(inheritedClasses.size()-1);
            ClassGuiProperties properties = (ClassGuiProperties)child.getAnnotation(ClassGuiProperties.class);
            if (properties!= null && properties.includeParent()) {
                Class<?> parent = child.getSuperclass();
                if (parent != null) {
                    inheritedClasses.add(parent);
                    continue;
                }
            }
            break;
        }
    }

    /**
     * Is the provided class equal to one of the classes in the included hierarchy
     * @param toTest The class to test
     * @return True if the class in ne of the ones in the inherited hierarchy
     */
    public boolean inheritsFrom(Class<?> toTest){
        for (Class<?> c : inheritedClasses) {
            if (toTest.equals(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the declared field from on the inheritedClasses
     * @param fieldName The field to get
     * @return The field, null if non exists
     */
    public Field getDeclaredField(String fieldName){
        Field field = null;
        for(Class<?> layer : inheritedClasses){
            try {
                field = layer.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
        return field;
    }

    /**
     * Get the lowest level of the hierarchy
     * @return The lowest level of the hierarchy
     */
    public Class<?> getBaseType() {
        return baseType;
    }
}
