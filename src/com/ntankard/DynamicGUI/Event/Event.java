package com.ntankard.DynamicGUI.Event;



import java.io.Serializable;

import com.ntankard.GuiUtil.BoundStructure_Generator;

/**
 * Created by Nicholas on 5/06/2016.
 */
public abstract class Event implements Serializable, BoundStructure_Generator {

    private static final long serialVersionUID = 3306006568441153813L;

    public enum State {UNINITIALIZED, USER_INPUT_NEEDED, USER_ACTION_NEEDED, READY}

    private State state;

    public abstract void execute();

    public BoundStructure_Generator clone(){
        return null;
    }

   // public BoundStructure_JPanel getEditPanel(){
    //    return null;
  //  }


}
