package com.ntankard.DynamicGUI.Composite;

import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Util.Updatable;

import java.util.List;

import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class DynamicGUI_EditableList<T> extends DynamicGUI_IntractableList<T> {

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Factories #######################################################
    //------------------------------------------------------------------------------------------------------------------

    public static <T> DynamicGUI_EditableList newIntractableList(List<T> objects, MemberClass mClass, Updatable master) {
        return newIntractableList(objects, mClass, false, ALWAYS_DISPLAY, master);
    }

    public static <T> DynamicGUI_EditableList newIntractableList(List<T> objects, MemberClass mClass, int verbosity, Updatable master) {
        return newIntractableList(objects, mClass, false, verbosity, master);
    }

    public static <T> DynamicGUI_EditableList newIntractableList(List<T> objects, MemberClass mClass, boolean addFilter, Updatable master) {
        return newIntractableList(objects, mClass, addFilter, ALWAYS_DISPLAY, master);
    }

    public static <T> DynamicGUI_EditableList newIntractableList(List<T> objects, MemberClass mClass, boolean addFilter, int verbosity, Updatable master) {
        return new DynamicGUI_EditableList<>(objects, mClass, false, addFilter, verbosity, master);
    }

    public static <T> DynamicGUI_EditableList newIntractableTable(List<T> objects, MemberClass mClass, Updatable master) {
        return newIntractableTable(objects, mClass, false, ALWAYS_DISPLAY, master);
    }

    public static <T> DynamicGUI_EditableList newIntractableTable(List<T> objects, MemberClass mClass, int verbosity, Updatable master) {
        return newIntractableTable(objects, mClass, false, verbosity, master);
    }

    public static <T> DynamicGUI_EditableList newIntractableTable(List<T> objects, MemberClass mClass, boolean addFilter, Updatable master) {
        return newIntractableTable(objects, mClass, addFilter, ALWAYS_DISPLAY, master);
    }

    public static <T> DynamicGUI_EditableList newIntractableTable(List<T> objects, MemberClass mClass, boolean addFilter, int verbosity, Updatable master) {
        return new DynamicGUI_EditableList<>(objects, mClass, true, addFilter, verbosity, master);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################## Core ##########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    private DynamicGUI_EditableList(List<T> base, MemberClass mClass, boolean isTable, boolean addFilter, int verbosity, Updatable master) {
        super(base, mClass, isTable, addFilter, verbosity, master);

        DynamicGUI_IntractableList.ListControl_Button newBtn = new DynamicGUI_IntractableList.ListControl_Button("New");
        newBtn.addActionListener(e -> {
            List selected = newBtn.getCoreList().getSelectedItems();
        });

        DynamicGUI_IntractableList.ListControl_Button editBtn = new DynamicGUI_IntractableList.ListControl_Button("Edit", ListControl_Button.EnableCondition.SINGLE,false);
        newBtn.addActionListener(e -> {
            List selected = editBtn.getCoreList().getSelectedItems();
        });

        DynamicGUI_IntractableList.ListControl_Button deleteBtn = new DynamicGUI_IntractableList.ListControl_Button("Delete", ListControl_Button.EnableCondition.MULTI,false);
        newBtn.addActionListener(e -> {
            List selected = deleteBtn.getCoreList().getSelectedItems();
        });

        this.addButton(newBtn);
        this.addButton(editBtn);
        this.addButton(deleteBtn);
    }
}