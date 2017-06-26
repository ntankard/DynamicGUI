package com.ntankard.DynamicGUI;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Nicholas on 30/06/2016.
 */
public class BoundExecute_JButton extends Bound_JButton {

    /**
     * The object to execute on
     */
    private Object toExecute;

    /**
     * The name of the function to execute
     */
    private String function;

    /**
     * Default constructor
     * @param toExecute
     * @param function
     */
    public BoundExecute_JButton(Object toExecute, String function) {
        super("None");
        this.toExecute = toExecute;
        this.function = function;
        addActionListener(e -> onClick());
    }

    private void onClick(){
        try {
            toExecute.getClass().getMethod(function).invoke(toExecute);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
