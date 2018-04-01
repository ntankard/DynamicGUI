package com.ntankard.DynamicGUI.Components.EditablePanel;

import com.ntankard.DynamicGUI.Components.EditablePanel.Primitives.BaseSwing.Bound_JComponent;
import com.ntankard.DynamicGUI.Components.EditablePanel.Primitives.BoundCalendar_JPanel;
import com.ntankard.DynamicGUI.Components.EditablePanel.Primitives.BoundDouble_JTextField;
import com.ntankard.DynamicGUI.Components.EditablePanel.Primitives.BoundInteger_JTextField;
import com.ntankard.DynamicGUI.Components.EditablePanel.Primitives.BoundString_JTextField;
import com.ntankard.DynamicGUI.Components.EditablePanel.Primitives.DataBinding.Bindable;
import com.ntankard.DynamicGUI.Components.EditablePanel.Primitives.DataBinding.BindableReflection;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.ClassExtension.Member;

import java.lang.reflect.Method;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Nicholas on 14/07/2017.
 */
public class BoundComposite_JPanelGenerator {

    /**
     * Generate a panel with all components in an object
     *
     * @param o The object to get the data from
     * @return The panel created from, and bound to the the object
     */
    public static BoundComposite_JPanel generate(Object o) {
        BoundComposite_JPanel panel = new BoundComposite_JPanel();
        List<Member> fields = new MemberClass(o).getMembers();// ReflectionGeneratorUtil.getFields(o);
        for (Member field : fields) {

            // pull out any provided properties
            boolean generate = true;
            boolean editable = true;
            if (field.getField() != null) {
                BoundStructure_Properties properties = field.getField().getAnnotation(BoundStructure_Properties.class);
                if (properties != null) {
                    generate = properties.generate();
                    editable = properties.editable();
                }
            } else {
                continue;
            }

            // generate the component
            if (generate) {
                add(panel, field.getName(), o, field.getGetter(), field.getSetter(), editable);
            }
        }

        // finalize the panel
        panel.finalizePanel();
        return panel;
    }

    /**
     * Add a field to the panel
     *
     * @param panel     The panel to add to
     * @param fieldName The name of the field
     * @param o         The object that stores the core data
     * @param getter    The getter of the data
     * @param setter    The setter of the data
     * @param editable  Can the data be changed
     */
    public static void add(BoundComposite_JPanel panel, String fieldName, Object o, Method getter, Method setter, boolean editable) {

        // create and add a component
        try {
            // try to generate a bindable primitive
            Bindable b = new BindableReflection(o, getter, setter, editable);
            Bound_JComponent component = getPrimitiveComponent(b);
            if (component != null) {
                panel.addDataAccess(fieldName, component, false);
                return;
            }

            // no primitive was supported, try to generate a larger panel of primitives
            BoundComposite_JPanel subPanel = BoundComposite_JPanelGenerator.generate(getter.invoke(o));
            panel.addPanelManager(fieldName, subPanel, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempt get create a JComponent around the object, if its a supported primitivee
     *
     * @param toAdd The data to build the component around
     * @return Bound_JComponent or null if its not supported
     */
    public static Bound_JComponent getPrimitiveComponent(Bindable toAdd) {
        Class toAddType = toAdd.get().getClass();
        if (toAddType == Double.class) {
            return new BoundDouble_JTextField(toAdd);
        }
        if (toAddType == Integer.class) {
            return new BoundInteger_JTextField(toAdd);
        }
        if (toAddType == String.class) {
            return new BoundString_JTextField(toAdd);
        }
        if (toAddType == GregorianCalendar.class) {
            return new BoundCalendar_JPanel(toAdd);
        }
        return null;
    }
}
