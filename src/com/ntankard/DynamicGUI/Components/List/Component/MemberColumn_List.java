package com.ntankard.DynamicGUI.Components.List.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayTable_Model;

public class MemberColumn_List extends MemberColumn {

    /**
     * Constructor, parameters are set from the DisplayProperties set to the member
     *
     * @param member The member this column is based around
     * @param model  The model used to generate the columns containing this render.
     */
    public MemberColumn_List(Member member, DynamicGUI_DisplayTable_Model model) {
        super(member, model);
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEditable() {
        return member.getSetter() != null && member.getSource() != null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object encode(Object value) {
        return value;
    }
}
