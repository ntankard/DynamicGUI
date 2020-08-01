package com.ntankard.dynamicGUI.Gui.Components.List.Component;

import com.ntankard.dynamicGUI.CoreObject.Field.DataField;
import com.ntankard.dynamicGUI.Gui.Components.List.DynamicGUI_DisplayTable_Model;

public class MemberColumn_List extends MemberColumn {

    /**
     * Constructor, parameters are set from the DisplayProperties set to the member
     *
     * @param dataField The DataField that this column is built around
     * @param model     The model used to generate the columns containing this render.
     */
    public MemberColumn_List(DataField<?> dataField, DynamicGUI_DisplayTable_Model model) {
        super(dataField, model);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEditable() {
        return getDataField().getDataCore().canEdit() && getDataField().getSource() != null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object encode(Object value) {
        return value;
    }
}
