package com.ntankard.DynamicGUI.Components.List.Component;

import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Components.List.DynamicGUI_DisplayTable_Model;

import javax.swing.*;
import java.util.List;

public class MemberColumn_List extends MemberColumn {

    /**
     * The possible options (must have toString)
     */
    private List options;

    /**
     * The ComboBox used to display all the values that the column can be set to
     */
    private JComboBox<String> cellComboBox = new JComboBox<>();

    /**
     * Constructor, parameters are set from the DisplayProperties set to the member
     *
     * @param member  The member this column is based around
     * @param model   The model used to generate the columns containing this render.
     * @param options The values that can be selected
     */
    public MemberColumn_List(Member member, DynamicGUI_DisplayTable_Model model, List options) {
        super(member, model);
        this.options = options;

        for (Object option : options) {
            cellComboBox.addItem(option.toString());
        }
    }

    /**
     * Get the ComboBox used to display all the values that the column can be set to
     *
     * @return The ComboBox used to display all the values that the column can be set to
     */
    public JComboBox<String> getCellComboBox() {
        return cellComboBox;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isEditable() {
        return member.getSetter() != null;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Object encode(Object value) {
        for (Object object : options) {
            if (object.toString().equals(value)) {
                return object;
            }
        }
        throw new RuntimeException("Imposable value selected");
    }
}
