package com.ntankard.dynamicGUI.Gui.Components.List.Component;

import com.ntankard.javaObjectDatabase.CoreObject.Field.DataField;
import com.ntankard.javaObjectDatabase.CoreObject.Field.Properties.Display_Properties;
import com.ntankard.dynamicGUI.Gui.Components.List.Component.Renderer.*;
import com.ntankard.dynamicGUI.Gui.Components.List.DynamicGUI_DisplayTable_Model;
import com.ntankard.dynamicGUI.Gui.Util.Decoder.*;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class MemberColumn {

    /**
     * The DataField that this column is built around
     */
    protected DataField<?> dataField;

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
     * @param dataField The DataField that this column is built around
     * @param model     The model used to generate the columns containing this render.
     */
    public MemberColumn(DataField<?> dataField, DynamicGUI_DisplayTable_Model model) {
        this.dataField = dataField;

        // Extract any properties
        Display_Properties properties = getDataField().getDisplayProperties();
        Display_Properties.DataType dataType = getDataField().getDisplayProperties().getDataType();
        order = getDataField().getDisplayProperties().getOrder();
        name = getDataField().getDisplayName();

        if (getDataField().getType().equals(Double.class)) {
            if (properties.getDataContext() == Display_Properties.DataContext.ZERO_BELOW_BAD) {
                renderer = new NegativeHighlightRenderer(model, false);
            } else if (properties.getDataContext() == Display_Properties.DataContext.ZERO_BINARY) {
                renderer = new NegativeHighlightRenderer(model, true);
            } else if (properties.getDataContext() == Display_Properties.DataContext.ZERO_SCALE) {
                renderer = new ScaleRenderer(model);
            } else if (properties.getDataContext() == Display_Properties.DataContext.ZERO_TARGET) {
                renderer = new NonZeroRenderer(model);
            }
        }
        if (getDataField().getType().equals(Boolean.class)) {
            if (getDataField().getDisplayProperties().getDataContext() == Display_Properties.DataContext.NOT_FALSE) {
                renderer = new FalseHighlightRenderer(model);
            }
        }

        // Fill in any missing properties
        if (renderer == null) {
            renderer = new Renderer(model);
        }

        // Build the decoder based on type
        Decoder decoder = null;
        if (getDataField().getType().equals(Calendar.class)) {
            decoder = new CalendarDecoder();
        } else if (getDataField().getType().equals(Double.class)) {
            if (dataType.equals(Display_Properties.DataType.CURRENCY)) {
                decoder = new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), getDataField().getDisplayName());
            } else if (dataType.equals(Display_Properties.DataType.CURRENCY_AUD)) {
                decoder = new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), getDataField().getDisplayName());
            } else if (dataType.equals(Display_Properties.DataType.CURRENCY_YEN)) {
                decoder = new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.JAPAN), getDataField().getDisplayName());
            } else {
                decoder = new DoubleDecoder(getDataField().getDisplayProperties().getDisplayDecimal());
            }
        } else if (getDataField().getType().equals(String.class)) {
            decoder = new StringDecoder();
        } else if (getDataField().getType().equals(Integer.class)) {
            decoder = new IntegerDecoder();
        }
        renderer.setDecoder(decoder);
    }

    /**
     * Set A user set source for the locale
     *
     * @param numberFormatSource A user set source for the locale
     */
    public void setNumberFormatSource(CurrencyDecoder_NumberFormatSource numberFormatSource) {
        Decoder decoder = renderer.getDecoder();
        if (decoder instanceof CurrencyDecoder) {
            CurrencyDecoder currencyDecoder = (CurrencyDecoder) decoder;
            currencyDecoder.setNumberFormatSource(numberFormatSource);
        }
    }

    /**
     * Can this Column be edited?
     *
     * @return True if this Column be edited?
     */
    public boolean isEditable() {
        if (!getDataField().getDataCore().canEdit() || getRenderer().getDecoder() == null) {
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
        return getRenderer().getDecoder().encode(value.toString());
    }

    //------------------------------------------------------------------------------------------------------------------
    //########################################### Standard accessors ###################################################
    //------------------------------------------------------------------------------------------------------------------

    public DataField<?> getDataField() {
        return dataField;
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
