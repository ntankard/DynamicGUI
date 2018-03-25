package com.ntankard.DynamicGUI.Components.List.Controls;

import com.ntankard.DynamicGUI.Components.List.Display.BoundArray;

import javax.swing.*;

public class ListControl_Button extends JButton{

    /**
     * The list containing this button
     */
    private BoundArray coreList;

    /**
     * Default constructor
     * @param name The name to put on the button
     */
    public ListControl_Button(String name){
        super(name);
    }

    /**
     * Called when the button is added to a BoundArray to register the parent
     * @param coreList The list to link to
     */
    public void setControllableList(BoundArray coreList){
        this.coreList = coreList;
    }

    /**
     * Accessor to allow the button (and its action action) to get access to the list it is to act on
     * @return The list containing this button
     */
    public BoundArray getCoreList(){
        return coreList;
    }
}
