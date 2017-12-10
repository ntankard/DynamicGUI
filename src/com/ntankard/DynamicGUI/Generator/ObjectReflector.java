package com.ntankard.DynamicGUI.Generator;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ObjectReflector {
    public static ArrayList<ObjectField> getFields(Object o) {
        ArrayList<ObjectField> fields = new ArrayList<>();
        ExtendedClass context = new ExtendedClass(o);

        for (Method getter : context.getBaseType().getMethods()) {

            // is this method an accessor for a field?
            String fieldName = getter.getName().replace("get", "");
            if (!getter.getName().contains("get") || fieldName.equals("")) {
                continue;
            }

            // is this a getter for a field at the right level (eg not one for Object)
            if (!context.inheritsFrom(getter.getDeclaringClass())) {
                continue;
            }

            // can the field be edited?
            Method setter = null;
            try {
                setter = context.getBaseType().getMethod("set" + fieldName, getter.getReturnType());
            } catch (NoSuchMethodException e) {
            }

            // pull out any provided properties
            Field field = context.getDeclaredField(fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1));
            if (field == null) {
                continue;
            }

            // verify the getter and setter
            try {
                /*if (getter.invoke(o) == null)
                    continue;*/
                if (setter != null)
                    if (setter.getParameterCount() != 1 || !getter.getReturnType().equals(setter.getParameterTypes()[0]))
                        continue;
            } catch (Exception e) {
                continue;
            }

            // generate the component
            fields.add(new ObjectField(o, fieldName, field, getter, setter));

        }
        return fields;
    }
}
