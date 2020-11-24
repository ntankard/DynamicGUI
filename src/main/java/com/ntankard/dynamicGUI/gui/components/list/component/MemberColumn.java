package com.ntankard.dynamicGUI.gui.components.list.component;

import com.ntankard.javaObjectDatabase.dataField.DataField_Schema;
import com.ntankard.dynamicGUI.javaObjectDatabase.Display_Properties;
import com.ntankard.dynamicGUI.gui.components.list.component.renderer.*;
import com.ntankard.dynamicGUI.gui.components.list.DynamicGUI_DisplayTable_Model;
import com.ntankard.dynamicGUI.gui.util.decoder.*;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class MemberColumn {

    /**
     * The DataField that this column is built around
     */
    protected DataField_Schema<?> dataFieldSchema;

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
     * @param dataFieldSchema The DataField that this column is built around
     * @param model           The model used to generate the columns containing this render.
     */
    public MemberColumn(DataField_Schema<?> dataFieldSchema, DynamicGUI_DisplayTable_Model model) {
        this.dataFieldSchema = dataFieldSchema;

        // Extract any properties
        Display_Properties properties = getDataFieldSchema().getProperty(Display_Properties.class);
        Display_Properties.DataType dataType = getDataFieldSchema().getProperty(Display_Properties.class).getDataType();
        order = getDataFieldSchema().getOrder();
        name = getDataFieldSchema().getDisplayName();

        if (getDataFieldSchema().getType().equals(Double.class)) {
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
        if (getDataFieldSchema().getType().equals(Boolean.class)) {
            if (getDataFieldSchema().getProperty(Display_Properties.class).getDataContext() == Display_Properties.DataContext.NOT_FALSE) {
                renderer = new FalseHighlightRenderer(model);
            }
        }

        // Fill in any missing properties
        if (renderer == null) {
            renderer = new Renderer(model);
        }

        // Build the decoder based on type
        Decoder decoder = null;
        if (getDataFieldSchema().getType().equals(Calendar.class)) {
            decoder = new CalendarDecoder();
        } else if (getDataFieldSchema().getType().equals(Double.class)) {
            if (dataType.equals(Display_Properties.DataType.CURRENCY)) {
                decoder = new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), getDataFieldSchema().getDisplayName());
            } else if (dataType.equals(Display_Properties.DataType.CURRENCY_AUD)) {
                decoder = new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.US), getDataFieldSchema().getDisplayName());
            } else if (dataType.equals(Display_Properties.DataType.CURRENCY_YEN)) {
                decoder = new CurrencyDecoder(NumberFormat.getCurrencyInstance(Locale.JAPAN), getDataFieldSchema().getDisplayName());
            } else {
                decoder = new DoubleDecoder(getDataFieldSchema().getProperty(Display_Properties.class).getDisplayDecimal());
            }
        } else if (getDataFieldSchema().getType().equals(String.class)) {
            decoder = new StringDecoder();
        } else if (getDataFieldSchema().getType().equals(Integer.class)) {
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
        if (!getDataFieldSchema().getCanEdit() || getRenderer().getDecoder() == null) {
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

    public DataField_Schema<?> getDataFieldSchema() {
        return dataFieldSchema;
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
