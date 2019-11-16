package com.ntankard.ClassExtension;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

public class Util {

    /**
     * Try to find a source of data for this object
     *
     * @param member The member to attached the data to
     * @return A source of data for the member
     */
    public static List getSetterSource(Member member, Object[] sources) {
        // is there a setter?
        Method setter = member.getSetter();
        if (setter == null) {
            return null;
        }

        // is a setter source available?
        SetterProperties properties = member.getSetter().getAnnotation(SetterProperties.class);
        String sourceMethod = properties != null ? properties.sourceMethod() : null;
        if (sourceMethod == null || sourceMethod.isEmpty()) {
            return null;
        }

        // search all possible sources of data for one that has the needed getter
        if (sources != null) {
            for (Object source : sources) {
                try {
                    // get the source data (an exception will be thrown if not available)
                    Method sourceGetter;
                    Object sourceData;
                    try {
                        sourceGetter = source.getClass().getDeclaredMethod(sourceMethod);
                        sourceData = sourceGetter.invoke(source);
                    } catch (Exception ignored) {
                        sourceGetter = source.getClass().getDeclaredMethod(sourceMethod, String.class);
                        sourceData = sourceGetter.invoke(source, member.getType().getSimpleName());
                    }

                    // is it a <String,Object> map
                    if (sourceData instanceof Map) {

                        // is any data available
                        Map mapSourceData = (Map) sourceData;
                        if (mapSourceData.isEmpty()) {
                            continue;
                        }

                        // is the value the same as the value we are expecting to set
                        if (!mapSourceData.values().toArray()[0].getClass().equals(member.getSetter().getParameterTypes()[0])) {
                            continue;
                        }

                        return new ArrayList<>(mapSourceData.values());

                    } else if (sourceData instanceof List) {

                        // is any data available
                        List listSourceData = (List) sourceData;
                        if (listSourceData.isEmpty()) {
                            continue;
                        }

                        // is the value the same as the value we are expecting to set
                        if (!member.getSetter().getParameterTypes()[0].isAssignableFrom(listSourceData.get(0).getClass())) {
                            continue;
                        }

                        return listSourceData;
                    }
                } catch (Exception ignored) {
                    throw new RuntimeException(ignored);
                }
            }
        }
        return null;
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

    /**
     * Find a class either by its full name or by its limited name is a package
     *
     * @param fullClassName   the full name to search for
     * @param rootPackageName The root package to look in if the full name cant be found
     * @return The found class or null
     */
    public static Class<?> classForName(String fullClassName, String rootPackageName) {
        Class aClass = null;
        try {
            aClass = Class.forName(fullClassName);
        } catch (ClassNotFoundException e) {
            System.out.println("WARNING, class " + fullClassName + " cant be found");
            String[] lines = fullClassName.split("\\.");
            String simpleClassName = lines[lines.length - 1];
            System.out.println("manually searching for " + simpleClassName);
            try {
                for (Class searchClass : getClasses(rootPackageName)) {
                    if (searchClass.getSimpleName().equals(simpleClassName)) {
                        aClass = searchClass;
                        break;
                    }
                }
                if (aClass == null) {
                    throw new RuntimeException(e);
                }
            } catch (ClassNotFoundException | IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return aClass;
    }
}
