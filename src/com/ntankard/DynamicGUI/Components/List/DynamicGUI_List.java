package com.ntankard.DynamicGUI.Components.List;

import com.ntankard.DynamicGUI.Components.List.Controls.ListControl_Button;
import com.ntankard.DynamicGUI.Components.List.Display.BoundArray;
import com.ntankard.DynamicGUI.Components.List.Display.BoundArray_JList;
import com.ntankard.DynamicGUI.Components.List.Display.BoundArray_JTable;
import com.ntankard.DynamicGUI.Util.ButtonPanel;
import com.ntankard.DynamicGUI.Util.Updatable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

import static com.ntankard.DynamicGUI.Components.List.DynamicGUI_List_Properties.TRACE_DISPLAY;

/**
 * Created by Nicholas on 26/06/2016.
 */
public class DynamicGUI_List extends JPanel implements Updatable {

    /**
     * GUI Objects
     */
    private BoundArray display;
    private ButtonPanel buttons;

    /**
     * The top level GUI
     */
    private Updatable master;

    //------------------------------------------------------------------------------------------------------------------
    //############################################## Constructors ######################################################
    //------------------------------------------------------------------------------------------------------------------

    public static DynamicGUI_List newStandardDisplayList(List objects, Updatable master) {
        return new DynamicGUI_List(master, new BoundArray_JList(objects, master));
    }

    public static DynamicGUI_List newStandardDisplayTable(List objects, Updatable master) {
        return newStandardDisplayTable(objects, master, TRACE_DISPLAY);
    }

    public static DynamicGUI_List newStandardDisplayTable(List objects, Updatable master, int verbosity) {
        return new DynamicGUI_List(master, new BoundArray_JTable(objects, master, verbosity));
    }

    private DynamicGUI_List(Updatable master, BoundArray display) {
        this.master = master;
        this.buttons = new ButtonPanel();
        this.display = display;
        createUIComponents();
        update();
    }

    /**
     * Create the GUI components
     */
    private void createUIComponents() {
        this.removeAll();

        this.setBorder(new EmptyBorder(12, 12, 12, 12));
        this.setLayout(new BorderLayout());
        this.add(display, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################ Buttons #########################################################
    //------------------------------------------------------------------------------------------------------------------

    public void addButton(ListControl_Button button) {
        buttons.addButton(button);
        button.setControllableList(display);
        createUIComponents();
    }

    //------------------------------------------------------------------------------------------------------------------
    //################################################# Utility ########################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void update() {
        display.update();
    }

    /**
     * @inheritDoc Bottom of the tree
     */
    @Override
    public void notifyUpdate() {
        master.notifyUpdate();
    }
}