package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.CurrencyDecoder_NumberFormatSource;
import com.ntankard.DynamicGUI.Util.Updatable;

import java.text.NumberFormat;

public class IntractableObject_Currency extends IntractableObject_Double {

    /**
     * The location used to get the currency
     */
    private NumberFormat numberFormat;

    /**
     * A user set source for the locale, numberFormat used if not set
     */
    private CurrencyDecoder_NumberFormatSource numberFormatSource = null;

    /**
     * Constructor
     *
     * @param baseMember   The member that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_Currency(ExecutableMember<Double> baseMember, boolean saveOnUpdate, int order, NumberFormat numberFormat, Updatable master) {
        super(baseMember, saveOnUpdate, order, master);
        this.numberFormat = numberFormat;
    }

    /**
     * Set A user set source for the locale
     *
     * @param localeSource A user set source for the locale
     */
    public void setLocaleInspector(CurrencyDecoder_NumberFormatSource localeSource) {
        this.numberFormatSource = localeSource;
        update();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setValue(Double value) {
        NumberFormat format = numberFormat;
        if (numberFormatSource != null) {
            format = numberFormatSource.getNumberFormat(baseMember.getObject());
        }

        if (format == null) {
            super.setValue(value);
            return;
        }

        value_txt.setText(format.format(value));
    }
}
