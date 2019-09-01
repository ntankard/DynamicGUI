package com.ntankard.DynamicGUI.Components.List.Types.Table;

import com.ntankard.ClassExtension.DisplayProperties;
import com.ntankard.ClassExtension.Member;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.*;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Renderer.NegativeHighlightRenderer;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Renderer.Renderer;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Renderer.ScaleRenderer;

import java.util.Calendar;
import java.util.Locale;

import static com.ntankard.ClassExtension.DisplayProperties.DataContext.*;
import static com.ntankard.ClassExtension.DisplayProperties.DataType;
import static com.ntankard.ClassExtension.DisplayProperties.DataType.*;

public class MemberColumn {

    /**
     * The core member
     */
    protected Member member;

    /**
     * The display order, Integer.MAX_VALUE if none is set
     */
    private int order = Integer.MAX_VALUE;

    /**
     * The display name of the column
     */
    private String name = "";

    /**
     * Any custom render for this column
     */
    private Renderer renderer = null;

    /**
     * Constructor, parameters are set from the DisplayProperties set to the member
     *
     * @param member The member this column is based around
     * @param model  The model used to generate the columns containing this render.
     */
    MemberColumn(Member member, DisplayList_JTable_Model model) {
        this.member = member;

        // Extract any properties
        DisplayProperties properties = this.member.getGetter().getAnnotation(DisplayProperties.class);
        DataType dataType = AS_CLASS;
        if (properties != null) {
            order = properties.order();
            name = properties.name();
            dataType = properties.dataType();

            if (member.getType().equals(Double.class)) {
                if (properties.dataContext() == ZERO_BELOW_BAD) {
                    renderer = new NegativeHighlightRenderer(model, false);
                } else if (properties.dataContext() == ZERO_BINARY) {
                    renderer = new NegativeHighlightRenderer(model, true);
                } else if (properties.dataContext() == ZERO_SCALE) {
                    renderer = new ScaleRenderer(model);
                }
            }
        }

        // Fill in any missing properties
        if (renderer == null) {
            renderer = new Renderer(model);
        }
        if (name.equals("")) {
            name = member.getName();
        }

        // Build the decoder based on type
        Decoder decoder = null;
        if (member.getType().equals(Calendar.class)) {
            decoder = new CalendarDecoder();
        } else if (member.getType().equals(Double.class)) {
            if (dataType.equals(CURRENCY)) {
                decoder = new CurrencyDecoder(Locale.US);
            } else if (dataType.equals(CURRENCY_AUD)) {
                decoder = new CurrencyDecoder(Locale.US);
            } else if (dataType.equals(CURRENCY_YEN)) {
                decoder = new CurrencyDecoder(Locale.JAPAN);
            } else {
                decoder = new DoubleDecoder();
            }
        }
        renderer.setDecoder(decoder);
    }

    /**
     * Set A user set source for the locale
     *
     * @param localeSource A user set source for the locale
     */
    public void setLocaleInspector(CurrencyDecoder_LocaleSource localeSource) {
        Decoder decoder = renderer.getDecoder();
        if (decoder instanceof CurrencyDecoder) {
            CurrencyDecoder currencyDecoder = (CurrencyDecoder) decoder;
            currencyDecoder.setLocaleInspector(localeSource);
        }
    }

    /**
     * Can this Column be edited?
     *
     * @return True if this Column be edited?
     */
    public boolean isEditable() {
        if (member.getSetter() == null || getRenderer().getDecoder() == null) {
            return false;
        }
        return renderer.getDecoder().isEditable();
    }

    /**
     * Convert the string into a the original object
     *
     * @param value The value to convert
     * @return The object made from the string
     */
    public Object encode(Object value) {
        return getRenderer().getDecoder().encode(value);
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public Member getMember() {
        return member;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public Renderer getRenderer() {
        return renderer;
    }
}
