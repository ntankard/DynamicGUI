package com.ntankard.ClassExtension;

import com.ntankard.DynamicGUI.Components.Object.SetterProperties;

import java.lang.reflect.Method;
import java.util.ArrayList;
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
        for (Object source : sources)
            try {
                // get the source data (an exception will be thrown if not available)
                Method sourceGetter = source.getClass().getDeclaredMethod(sourceMethod);
                Object sourceData = sourceGetter.invoke(source);

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
                    if (!listSourceData.get(0).getClass().equals(member.getSetter().getParameterTypes()[0])) {
                        continue;
                    }

                    return listSourceData;
                }
            } catch (Exception ignored) {
            }
        return null;
    }
}
