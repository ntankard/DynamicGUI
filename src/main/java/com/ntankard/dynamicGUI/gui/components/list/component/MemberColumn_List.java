package com.ntankard.dynamicGUI.gui.components.list.component;

import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.dynamicGUI.gui.components.list.DynamicGUI_DisplayTable_Model;

public class MemberColumn_List extends MemberColumn {

    /**
     * Constructor, parameters are set from the DisplayProperties set to the member
     *
     * @param dataFieldSchema The DataField that this column is built around
     * @param model     The model used to generate the columns containing this render.
     */
    public MemberColumn_List(DataField_Schema<?> dataFieldSchema, DynamicGUI_DisplayTable_Model model) {
        super(dataFieldSchema, model);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEditable() {
        return getDataFieldSchema().getCanEdit() && getDataFieldSchema().getSource() != null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object encode(Object value) {
        return value;
    }
}
