package com.ntankard.ClassExtension;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public class Util {

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     */
    public static Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[0]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : Objects.requireNonNull(files)) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                if (!file.getName().contains("$")) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
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
