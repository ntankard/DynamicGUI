package com.ntankard.dynamicGUI.Gui.Components.Object;

import com.ntankard.dynamicGUI.Gui.Components.Object.Component.IntractableObject;
import com.ntankard.dynamicGUI.Gui.Components.Object.Component.IntractableObject_Enum;
import com.ntankard.dynamicGUI.Gui.Components.Object.Component.IntractableObject_List;
import com.ntankard.dynamicGUI.Gui.Components.Object.Component.IntractableObject_String;
import com.ntankard.dynamicGUI.Gui.Util.Containers.PanelContainer;
import com.ntankard.dynamicGUI.Gui.Util.Decoder.*;
import com.ntankard.dynamicGUI.Gui.Util.Update.Updatable;
import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField_Schema;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties;
import com.ntankard.javaObjectDatabase.CoreObject.DataObject;
import com.ntankard.javaObjectDatabase.Database.TrackingDatabase_Schema;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class DynamicGUI_IntractableObject_Impl<T extends DataObject> extends PanelContainer {

    /**
     * The instance to interact with
     */
    private T baseInstance;

    /**
     * The kind of object used to generate this panel
     */
    private Class aClass;

    /**
     * What level of verbosity should be shown? (compared against MemberProperties verbosity)
     */
    private int verbosity;

    /**
     * All the panels generated from the object
     */
    private List<IntractableObject> intractableObjects = new ArrayList<>();

    /**
     * Should the action of the panel be done as soon as an update is received? or on command
     */
    private boolean saveOnUpdate;

    /**
     * A user set source for the locale, numberFormat used if not set
     */
    private CurrencyDecoder_NumberFormatSource localeSource;

    /**
     * Constructor
     *
     * @param baseInstance The instance to interact with
     * @param master       The top level GUI
     */
    public DynamicGUI_IntractableObject_Impl(T baseInstance, boolean vertical, Updatable master) {
        super(vertical, master);
        this.baseInstance = baseInstance;
        this.verbosity = Display_Properties.ALWAYS_DISPLAY;
        this.aClass = baseInstance.getClass();
        this.saveOnUpdate = true;

        createUIComponents();
        update();
    }

    /**
     * Set should the action of the panel be done as soon as an update is received? or on command
     *
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @return This
     */
    public DynamicGUI_IntractableObject_Impl<T> setSaveOnUpdate(boolean saveOnUpdate) {
        this.saveOnUpdate = saveOnUpdate;
        createUIComponents();
        update();
        return this;
    }

    /**
     * Set what level of verbosity should be shown?
     *
     * @param verbosity What level of verbosity should be shown?
     * @return This
     */
    public DynamicGUI_IntractableObject_Impl<T> setVerbosity(int verbosity) {
        this.verbosity = verbosity;
        createUIComponents();
        update();
        return this;
    }

    /**
     * Set a user set source for the locale, numberFormat used if not set
     *
     * @param localeSource A user set source for the locale, numberFormat used if not set
     * @return This
     */
    public DynamicGUI_IntractableObject_Impl<T> setLocaleSource(CurrencyDecoder_NumberFormatSource localeSource) {
        this.localeSource = localeSource;
        createUIComponents();
        update();
        return this;
    }

    /**
     * Execute all action of this panel based on the buffed value
     */
    public void execute() {
        for (IntractableObject t : intractableObjects) {
            t.execute();
        }
    }

    /**
     * Discard all change made by the user and revert the panel to its original state
     */
    public void discard() {
        for (IntractableObject t : intractableObjects) {
            t.discard();
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    //############################################# Extended methods ###################################################
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @inheritDoc
     */
    @Override
    protected void createUIComponents() {
        super.createUIComponents();
        intractableObjects.clear();

        for (DataField_Schema dataFieldSchema : TrackingDatabase_Schema.getFieldContainer(aClass).getVerbosityDataFields(verbosity)) {


            // find a compatible filter type
            IntractableObject<?> intractableObject;
            Class<?> theClass = dataFieldSchema.getType();

            Display_Properties.DataType dataType = dataFieldSchema.getDisplayProperties().getDataType();
            int order = dataFieldSchema.getDisplayProperties().getOrder();

            if (theClass.isEnum()) {
                intractableObject = new IntractableObject_Enum(dataFieldSchema, baseInstance, saveOnUpdate, order, this);
            } else if (theClass.equals(String.class)) {
                intractableObject = new IntractableObject_String<String>(dataFieldSchema, baseInstance, saveOnUpdate, order, new StringDecoder(), this);
            } else if (theClass.equals(Double.class)) {
                if (dataType.equals(Display_Properties.DataType.CURRENCY)) {
                    intractableObject = new IntractableObject_String<Double>(dataFieldSchema, baseInstance, saveOnUpdate, order, new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), dataFieldSchema.getDisplayName(), localeSource), this);
                } else if (dataType.equals(Display_Properties.DataType.CURRENCY_AUD)) {
                    intractableObject = new IntractableObject_String<Double>(dataFieldSchema, baseInstance, saveOnUpdate, order, new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), dataFieldSchema.getDisplayName(), localeSource), this);
                } else if (dataType.equals(Display_Properties.DataType.CURRENCY_YEN)) {
                    intractableObject = new IntractableObject_String<Double>(dataFieldSchema, baseInstance, saveOnUpdate, order, new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.JAPAN), dataFieldSchema.getDisplayName(), localeSource), this);
                } else {
                    intractableObject = new IntractableObject_String<Double>(dataFieldSchema, baseInstance, saveOnUpdate, order, new DoubleDecoder(dataFieldSchema.getDisplayProperties().getDisplayDecimal()), this);
                }
            } else if (theClass.equals(Integer.class)) {
                intractableObject = new IntractableObject_String<Integer>(dataFieldSchema, baseInstance, saveOnUpdate, order, new IntegerDecoder(), this);
            } else {
                if (dataFieldSchema.getSource() != null) {
                    intractableObject = new IntractableObject_List(dataFieldSchema, baseInstance, saveOnUpdate, order, this);
                } else {
                    intractableObject = new IntractableObject_String<Object>(dataFieldSchema, baseInstance, saveOnUpdate, order, new ToStringDecoder(), this);
                }
            }
            intractableObjects.add(intractableObject);
        }

        intractableObjects.sort(Comparator.comparingInt(IntractableObject::getOrder));

        for (IntractableObject intractableObject : intractableObjects) {
            addMember(intractableObject);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        for (IntractableObject intractableObject : intractableObjects) {
            intractableObject.update();
        }
    }
}
