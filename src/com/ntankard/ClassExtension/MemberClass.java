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
        return methods.toArray(new Method[0]);
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
    private Method[] getAccessors() {
        List<Method> methods = new ArrayList<>();
        for (Method m : getDeclaredMethods()) {
            if (Modifier.isPublic(m.getModifiers()) && !Modifier.isAbstract(m.getModifiers()) && (m.getName().contains("get") || m.getName().contains("is") || m.getName().contains("has"))) {

                // Check to see if you have the same method twice
                boolean wasAdded = false;
                for (Method added : methods) {
                    if (m.getName().equals(added.getName())) {
                        if (m.getParameterCount() == added.getParameterCount()) {
                            Class<?> mType = m.getReturnType();
                            Class<?> addedType = added.getReturnType();

                            // Find the one that is lower on the tree
                            if (mType.isAssignableFrom(addedType)) {
                                wasAdded = true;
                            } else if (addedType.isAssignableFrom(mType)) {
                                methods.remove(added);
                                methods.add(m);
                                wasAdded = true;
                            } else {
                                throw new RuntimeException("Imposable class");
                            }
                        }
                    }
                }
                if (!wasAdded) {
                    methods.add(m);
                }
            }
        }

        return methods.toArray(new Method[0]);
    }

    /**
     * Get all members for this class and its included layers at the listed verbosity level
     *
     * @param verbosity The verbosity level to filter on
     * @return A list of all members for this class and its included layers at the listed verbosity level
     */
    public List<Member> getVerbosityMembers(int verbosity) {
        return getVerbosityMembers(verbosity, true);
    }

    /**
     * Get all members for this class and its included layers at the listed verbosity level
     *
     * @param verbosity          The verbosity level to filter on
     * @param checkShouldDisplay Should the MemberProperties shouldDisplay flag be looked at?
     * @return A list of all members for this class and its included layers at the listed verbosity level
     */
    public List<Member> getVerbosityMembers(int verbosity, boolean checkShouldDisplay) {
        return getVerbosityMembers_impl(verbosity, null, checkShouldDisplay);
    }

    /**
     * Get all members for this class and its included layers at the listed verbosity level in an executable form
     *
     * @param verbosity The verbosity level to filter on
     * @param o         The object to bind to all returns to make them executable
     * @return A list of all members for this class and its included layers at the listed verbosity level in an executable form
     */
    public List<ExecutableMember> getVerbosityMembers(int verbosity, Object o) {
        return getVerbosityMembers_impl(verbosity, o, true);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Impl ##########################################################
    //------------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    private <T extends Member> List<T> getVerbosityMembers_impl(int verbosity, Object o, boolean checkShouldDisplay) {
        List<T> fields = new ArrayList<>();
        for (Member f : getAllMembers_impl(o)) {
            MemberProperties properties = f.getGetter().getAnnotation(MemberProperties.class);
            if (properties != null) {
                if (properties.verbosityLevel() > verbosity) {
                    continue;
                }
                if (checkShouldDisplay && !properties.shouldDisplay()) {
                    continue;
                }
            }
            fields.add((T) f);
        }
        return fields;
    }

    @SuppressWarnings("unchecked")
    private <T extends Member> List<T> getAllMembers_impl(Object o) {
        List<T> fields = new ArrayList<>();

        for (Method getter : getAccessors()) {
            if (getter.getParameterCount() == 0) {
                Member member;
                if (o == null) {
                    member = new Member(this, getter);
                } else {
                    member = new ExecutableMember(this, getter, o);
                }

                fields.add((T) member);
            }
        }
        return fields;
    }
}