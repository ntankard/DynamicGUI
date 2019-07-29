package com.ntankard.DynamicGUI.Components.Object;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.ClassExtension.Member;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.Object.Component.IntractableObject;
import com.ntankard.DynamicGUI.Components.Object.Component.IntractableObject_Enum;
import com.ntankard.DynamicGUI.Components.Object.Component.IntractableObject_List;
import com.ntankard.DynamicGUI.Components.Object.Component.IntractableObject_String;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicGUI_IntractableObject<T> extends Updatable.UpdatableJPanel {

    /**
     * The instance to interact with
     */
    private T baseInstance;

    /**
     * The kind of object used to generate this panel
     */
    private MemberClass mClass;

    /**
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private int verbosity;

    /**
     * Sources of data that can be set for various objects
     */
    private Object[] sources;

    /**
     * All the panels generated from the object
     */
    private List<IntractableObject> intractableObjects = new ArrayList<>();

    /**
     * Constructor
     *
     * @param baseInstance The instance to interact with
     * @param verbosity    What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param master       The top level GUI
     * @param sources      Sources of data that can be set for various objects
     */
    public DynamicGUI_IntractableObject(T baseInstance, int verbosity, Updatable master, Object... sources) {
        super(master);
        this.baseInstance = baseInstance;
        this.verbosity = verbosity;
        this.mClass = new MemberClass(baseInstance.getClass());
        this.sources = sources;

        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();
        this.setLayout(new GridBagLayout());

        GridBagConstraints componentC = new GridBagConstraints();
        componentC.weightx = 1;
        componentC.fill = GridBagConstraints.BOTH;

        int index = 0;
        for (ExecutableMember member : mClass.getVerbosityMembers(verbosity, baseInstance)) {

            // find a compatible filter type
            IntractableObject intractableObject;
            Class<?> theClass = member.getType();
            if (theClass.isEnum()) {
                intractableObject = new IntractableObject_Enum(member, this);
            } else if (theClass.equals(String.class)) {
                intractableObject = new IntractableObject_String(member, this);
            } else {
                List options = getSetterSource(member);
                if (options != null) {
                    intractableObject = new IntractableObject_List(member, options, this);
                } else {
                    continue; // No supported panel available
                }
            }
            intractableObjects.add(intractableObject);

            // add the new filter component
            componentC.gridy = index;
            this.add(intractableObject, componentC);
            index++;
        }

        // add final spacer to ensure the content stays at the top of the panel
        GridBagConstraints spacerC = new GridBagConstraints();
        spacerC.weighty = 1;
        spacerC.gridy = index;
        this.add(new JSeparator(), spacerC);
    }

    /**
     * Try to find a source of data for this object
     *
     * @param member The member to attached the data to
     * @return A source of data for the member
     */
    private List getSetterSource(Member member) {
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

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        intractableObjects.forEach(Updatable::update);
    }
}