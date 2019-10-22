package com.ntankard.DynamicGUI.Components.Object;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.ClassExtension.MemberClass;
import com.ntankard.DynamicGUI.Util.Decoder.CurrencyDecoder;
import com.ntankard.DynamicGUI.Util.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.DynamicGUI.Util.Decoder.DoubleDecoder;
import com.ntankard.DynamicGUI.Util.Decoder.StringDecoder;
import com.ntankard.DynamicGUI.Components.Object.Component.*;
import com.ntankard.DynamicGUI.Util.Swing.Containers.PanelContainer;
import com.ntankard.DynamicGUI.Util.Updatable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.ntankard.ClassExtension.DisplayProperties.DataType.*;
import static com.ntankard.ClassExtension.Util.getSetterSource;

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
     * Sources of data that can be set for various objects
     */
    private Object[] sources;

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
     * @param verbosity    What level of verbosity should be shown? (compared against MemberProperties verbosity)
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param localeSource A user set source for the locale, numberFormat used if not set
     * @param master       The top level GUI
     * @param sources      Sources of data that can be set for various objects
     */
    DynamicGUI_IntractableObject_Impl(T baseInstance, int verbosity, boolean saveOnUpdate, CurrencyDecoder_NumberFormatSource localeSource, Updatable master, Object... sources) {
        super(master);
        this.baseInstance = baseInstance;
        this.verbosity = verbosity;
        this.mClass = new MemberClass(baseInstance.getClass());
        this.sources = sources;
        this.saveOnUpdate = saveOnUpdate;
        this.localeSource = localeSource;

        createUIComponents();
        update();
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
                    intractableObject = new IntractableObject_String<Double>(member, saveOnUpdate, order, new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), localeSource), this);
                } else if (dataType.equals(CURRENCY_AUD)) {
                    intractableObject = new IntractableObject_String<Double>(member, saveOnUpdate, order, new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), localeSource), this);
                } else if (dataType.equals(CURRENCY_YEN)) {
                    intractableObject = new IntractableObject_String<Double>(member, saveOnUpdate, order, new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.JAPAN), localeSource), this);
                } else {
                    intractableObject = new IntractableObject_String<Double>(member, saveOnUpdate, order, new DoubleDecoder(), this);
                }
            } else {
                List options = getSetterSource(member, sources);
                if (options != null) {
                    intractableObject = new IntractableObject_List(member, saveOnUpdate, order, options, this);
                } else {
                    continue; // No supported panel available
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
