package com.ntankard.DynamicGUI.Generator;

import com.ntankard.DynamicGUI.Bindable;
import com.ntankard.DynamicGUI.Components.*;
import com.ntankard.DynamicGUI.EditableBindable;
import com.ntankard.DynamicGUI.GuiUtil.BoundStructure_Generator;
import com.ntankard.DynamicGUI.NonEditableBindable;
import com.ntankard.DynamicGUI.Unused.BoundEvent_JButton;
import javafx.util.Pair;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by Nicholas on 14/07/2017.
 */
public class ReflectionGuiGenerator {
    BoundComposite_JPanel panel = new BoundComposite_JPanel();

    public BoundComposite_JPanel get(Object o)  {

        Class<?> baseType = o.getClass();
        ArrayList<Class<?>> inheritedClasses = new ArrayList<>();
        inheritedClasses.add(baseType);

        // find all relevant classes in the hierarchy and pull any relevant annotations
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

        for(Method getter : baseType.getMethods() ){

            // is this method an accessor for a field?
            String fieldName = getter.getName().replace("get", "");
            if(!getter.getName().contains("get") || fieldName.equals("")){
                continue;
            }

            // is this a getter for a field at the right level (eg not one for Object)
            boolean validLayer = false;
            for(Class<?> c : inheritedClasses)
                if(getter.getDeclaringClass().equals(c))
                    validLayer = true;
            if(!validLayer)
                continue;

            // can the field be edited?
            Method setter = null;
            try {
                setter = baseType.getMethod("set" + fieldName, getter.getReturnType());
            } catch (NoSuchMethodException e) {}

            // pull out any provided properties
            boolean generate = true;
            boolean editable = true;
            for(Class<?> layer : inheritedClasses){
                try {
                    Field field = layer.getDeclaredField(fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1));
                    FieldGuiProperties properties = field.getAnnotation(FieldGuiProperties.class);
                    if(properties != null){
                        generate = properties.generate();
                        editable = properties.editable();
                    }
                } catch (NoSuchFieldException e) {}
            }

            // generate the component
            try {
                if (generate) {
                    if (setter != null && editable) {
                        add(fieldName, new EditableBindable(o, getter, setter));
                    } else {
                        add(fieldName, new NonEditableBindable(o, getter));
                    }
                }
            }catch (Exception e) {}
        }

        // finalize the panel
        panel.finalizePanel();
        return panel;
    }

    // Defaults
    public void add(String name, Bindable toAdd){add(name,toAdd,false);}
    public <T> void add(String name, Bindable data, ArrayList<T> options){add(name,data,options,false);}
    public void add(String name, Bound_JComponent toAdd){add(name,toAdd,false);}

    /**
     * Add a new row, automatically figures out what kind and binds appropriately
     * @param name
     * @param toAdd
     */
    public void add(String name, Bindable toAdd, Boolean isRestricted){
        if(toAdd == null || toAdd.get() == null){
            return;
        }

        Class toAddType = toAdd.get().getClass();
        if(toAddType == Double.class){
            panel.addDataAccess(name,new BoundDouble_JTextField(toAdd),isRestricted);
            return;
        }
        if(toAddType == Integer.class){
            panel.addDataAccess(name,new BoundInteger_JTextField(toAdd),isRestricted);
            return;
        }
        if(toAddType == String.class){
            panel.addDataAccess(name,new BoundString_JTextField(toAdd),isRestricted);
            return;
        }
        if(toAddType == GregorianCalendar.class){   //@TODO make this more abstract
            panel.addDataAccess(name,new BoundCalendar_JPanel(toAdd),isRestricted);
            return;
        }


        ReflectionGuiGenerator r = new ReflectionGuiGenerator();
        panel.addPanelManager(name, r.get(toAdd.get()),isRestricted);

        throw new IllegalStateException("Option not supported");
    }

    /**
     * Add for combo box types
     * @param name
     * @param data
     * @param options
     * @param isRestricted
     */
    public <T> void add(String name, Bindable<T> data, ArrayList<T> options, Boolean isRestricted) {

        if(options == null){
            options = new ArrayList<>();
            options.add(data.get());
        }

        if(options.get(0).getClass() == Pair.class){
            panel.addDataAccess(name,new BoundPrimitivePair_JComboBox(data,options),isRestricted);
            return;
        }else{
            panel.addDataAccess(name,new BoundOptions_JComboBox(data,options),isRestricted);
            return;
        }
    }

    /**
     * Add for panels
     * @param name
     * @param toAdd
     */
    public void add(String name, Bound_JComponent toAdd, Boolean isRestricted){
        panel.addPanelManager(name,(BoundComposite_JPanel)toAdd,isRestricted);
    }

    /**
     * Add for buttons
     * @param name
     * @param toAdd
     */
    public void add(String name, BoundStructure_Generator toAdd){
        // add(name,toAdd,false);
    }



}
