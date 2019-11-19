package com.ntankard.DynamicGUI.Components.Object;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Components.Object.Component.IntractableObject;
import com.ntankard.DynamicGUI.Components.Object.Component.IntractableObject_Enum;
import com.ntankard.DynamicGUI.Components.Object.Component.IntractableObject_List;
import com.ntankard.DynamicGUI.Components.Object.Component.IntractableObject_String;
import com.ntankard.DynamicGUI.Util.Containers.PanelContainer;
import com.ntankard.DynamicGUI.Util.Decoder.*;
import com.ntankard.DynamicGUI.Util.Update.Updatable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.*;
import static com.ntankard.ClassExtension.MemberProperties.ALWAYS_DISPLAY;

public class DynamicGUI_IntractableObject_Impl<T> extends PanelContainer {

    /**
     * The instance to interact with
     */
    private T baseInstance;

    /**
     * The kind of object used to generate this panel
     */
    private MemberClass mClass;

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
        this.verbosity = ALWAYS_DISPLAY;
        this.mClass = new MemberClass(baseInstance.getClass());
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

        for (ExecutableMember member : mClass.getVerbosityMembers(verbosity, baseInstance)) {

            // find a compatible filter type
            IntractableObject intractableObject;
            Class<?> theClass = member.getType();

            DisplayProperties properties = member.getGetter().getAnnotation(DisplayProperties.class);
            DisplayProperties.DataType dataType = AS_CLASS;
            int order = Integer.MAX_VALUE;
            if (properties != null) {
                dataType = properties.dataType();
                order = properties.order();
            }

            if (theClass.isEnum()) {
                intractableObject = new IntractableObject_Enum(member, saveOnUpdate, order, this);
            } else if (theClass.equals(String.class)) {
                intractableObject = new IntractableObject_String<String>(member, saveOnUpdate, order, new StringDecoder(), this);
            } else if (theClass.equals(Double.class)) {
                if (dataType.equals(CURRENCY)) {
                    intractableObject = new IntractableObject_String<Double>(member, saveOnUpdate, order, new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), member.getName(), localeSource), this);
                } else if (dataType.equals(CURRENCY_AUD)) {
                    intractableObject = new IntractableObject_String<Double>(member, saveOnUpdate, order, new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), member.getName(), localeSource), this);
                } else if (dataType.equals(CURRENCY_YEN)) {
                    intractableObject = new IntractableObject_String<Double>(member, saveOnUpdate, order, new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.JAPAN), member.getName(), localeSource), this);
                } else {
                    intractableObject = new IntractableObject_String<Double>(member, saveOnUpdate, order, new DoubleDecoder(), this);
                }
            } else {
                if (member.getSource() != null) {
                    intractableObject = new IntractableObject_List(member, saveOnUpdate, order, this);
                } else {
                    intractableObject = new IntractableObject_String<Object>(member, saveOnUpdate, order, new ToStringDecoder(), this);
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
