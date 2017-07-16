package com.ntankard.DynamicGUI.Generator;

import com.ntankard.DynamicGUI.DataBinding.Bindable;
import com.ntankard.DynamicGUI.DataBinding.BindableReflection;
import com.ntankard.DynamicGUI.Components.BaseSwing.Bound_JComponent;
import com.ntankard.DynamicGUI.Components.Container.BoundComposite_JPanel;
import com.ntankard.DynamicGUI.Components.Primitives.BoundCalendar_JPanel;
import com.ntankard.DynamicGUI.Components.Primitives.BoundDouble_JTextField;
import com.ntankard.DynamicGUI.Components.Primitives.BoundInteger_JTextField;
import com.ntankard.DynamicGUI.Components.Primitives.BoundString_JTextField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.GregorianCalendar;

/**
 * Created by Nicholas on 14/07/2017.
 */
public class ReflectionGuiGenerator {

    /**
     * Generate a panel with all components in an object
     * @param o The object to get the data from
     * @return The panel created from, and bound to the the object
     */
    public static BoundComposite_JPanel generate(Object o)  {
        BoundComposite_JPanel panel = new BoundComposite_JPanel();
        ExtendedClass context = new ExtendedClass(o);

        for(Method getter : context.getBaseType().getMethods() ) {

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
            boolean generate = true;
            boolean editable = true;
            Field field = context.getDeclaredField(fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1));
            if (field != null){
                FieldGuiProperties properties = field.getAnnotation(FieldGuiProperties.class);
                if (properties != null) {
                    generate = properties.generate();
                    editable = properties.editable();
                }
            }

            // generate the component
            if (generate) {
                add(panel,fieldName,o,getter,setter,editable);
            }
        }

        // finalize the panel
        panel.finalizePanel();
        return panel;
    }

    /**
     * Add a field to the panel
     * @param panel The panel to add to
     * @param fieldName The name of the field
     * @param o The object that stores the core data
     * @param getter The getter of the data
     * @param setter The setter of the data
     * @param editable Can the data be changed
     */
    public static void add(BoundComposite_JPanel panel, String fieldName,Object o, Method getter, Method setter, boolean editable){

        // verify that the field, getter and setter combination are valid
        try {
            if(getter.invoke(o) == null)
                return;
            if(setter != null)
                if (setter.getParameterCount() != 1 || !getter.getReturnType().equals(setter.getParameterTypes()[0]))
                    return;
        } catch (Exception e) {
            return;
        }

        // create and add a component
        try {
            // try to generate a bindable primitive
            Bindable b = new BindableReflection(o, getter, setter,editable);
            Bound_JComponent component = getPrimitiveComponent(b);
            if(component != null){
                panel.addDataAccess(fieldName,component,false);
                return;
            }

            // no primitive was supported, try to generate a larger panel of primitives
            BoundComposite_JPanel subPanel = ReflectionGuiGenerator.generate(getter.invoke(o));
            panel.addPanelManager(fieldName,subPanel,false);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempt get create a JComponent around the object, if its a supported primitivee
     * @param toAdd The data to build the component around
     * @return Bound_JComponent or null if its not supported
     */
    public static Bound_JComponent getPrimitiveComponent(Bindable toAdd){
        Class toAddType = toAdd.get().getClass();
        if(toAddType == Double.class){
            return  new BoundDouble_JTextField(toAdd);
        }
        if(toAddType == Integer.class){
            return new BoundInteger_JTextField(toAdd);
        }
        if(toAddType == String.class){
            return new BoundString_JTextField(toAdd);
        }
        if(toAddType == GregorianCalendar.class){
            return new BoundCalendar_JPanel(toAdd);
        }
        return null;
    }
}
