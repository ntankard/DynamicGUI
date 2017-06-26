package com.ntankard.DynamicGUI;

import com.ntankard.DynamicGUI.Event.Event;
import com.ntankard.GuiUtil.BoundStructure_Dialog;

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
        BoundStructure_Dialog.openPanel(data.get().getPanel());
    }
}
