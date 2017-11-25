package com.ntankard.DynamicGUI.Unused;

import com.ntankard.DynamicGUI.DataBinding.Bindable;
import com.ntankard.DynamicGUI.Unused.Event.Event;
import com.ntankard.DynamicGUI.GuiUtil.BoundStructure_Dialog;

/**
 * Created by Nicholas on 30/06/2016.
 */
public class BoundEvent_JButton extends Bound_JButton {

    private Bindable<Event> data;

    public BoundEvent_JButton(String name, Bindable<Event> data) {
        super(name);
        this.data = data;
        addActionListener(e -> onClick());
    }

    private void onClick(){
       // BoundStructure_Dialog.openPanel(data.get().getPanel());
    }
}
