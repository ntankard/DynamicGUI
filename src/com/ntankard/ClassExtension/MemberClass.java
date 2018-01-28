package com.ntankard.ClassExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A replacement for Class that includes a few layers higher in the hierarchy and access to accessible members(based on ClassExtensionProperties)
 */
public class MemberClass {

    /**
     * The lowest class
     */
    private Class<?> baseType;

    /**
     * The classes baseType extends that should be used as part of Gui generation
     */
    private List<Class<?>> inheritedClasses = new ArrayList<>();

    /**
     * Default constructor
     *
     * @param object The object to reflect
     */
    public MemberClass(Object object) {
        this(object.getClass());
    }

    /**
     * Default constructor
     *
     * @param aClass The object to reflect
     */
    public MemberClass(Class<?> aClass) {
        baseType = aClass;
        inheritedClasses.add(baseType);

        while (true) {
            Class child = inheritedClasses.get(inheritedClasses.size() - 1);
            ClassExtensionProperties properties = (ClassExtensionProperties) child.getAnnotation(ClassExtensionProperties.class);
            if (properties != null && properties.includeParent()) {
                Class<?> parent = child.getSuperclass();
                if (parent != null) {
                    inheritedClasses.add(parent);
                    continue;
                }
            }
            break;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################ Class<?> methods ####################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Class.getDeclaredMethods() for each of the included layers
     */
    public Method[] getDeclaredMethods() {
        List<Method> methods = new ArrayList<>();
        inheritedClasses.forEach(aClass -> methods.addAll(Arrays.asList(aClass.getDeclaredMethods())));
        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * Class.getMethod(...) on the baseType
     */
    public Method getMethod(String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        return baseType.getMethod(name, parameterTypes);
    }

    /**
     * Class.getField on the baseType
     */
    public Field getField(String name) throws NoSuchFieldException {
        return baseType.getField(name);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Custom #########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * Get all methods that represent standard public accessors for all included layers (get, is, has)
     *
     * @return All methods that represent standard public accessors for all included layers(get, is, has)
     */
    public Method[] getAccessors() {
        List<Method> methods = new ArrayList<>();
        for (Method m : getDeclaredMethods()) {
            if (Modifier.isPublic(m.getModifiers()) && (m.getName().contains("get") || m.getName().contains("is") || m.getName().contains("has"))) {
                methods.add(m);
            }
        }
        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * Get all members for this class and its included layers
     *
     * @return All members for this class and its included layers
     */
    public List<Member> getMembers() {
        ArrayList<Member> fields = new ArrayList<>();

        for (Method getter : getAccessors()) {
            fields.add(new Member(this, getter));
        }
        return fields;
    }
}