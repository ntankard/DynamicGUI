package com.ntankard.DynamicGUI.Components.Object.Component;

import com.ntankard.ClassExtension.ExecutableMember;
import com.ntankard.DynamicGUI.Components.List.Types.Table.Decoder.CurrencyDecoder_LocaleSource;
import com.ntankard.DynamicGUI.Util.Updatable;

import java.text.NumberFormat;
import java.util.Locale;

public class IntractableObject_Currency extends IntractableObject_Double {

    /**
     * The location used to get the currency
     */
    private Locale currencyLocale;

    /**
     * A user set source for the locale, currencyLocale used if not set
     */
    private CurrencyDecoder_LocaleSource localeSource = null;

    /**
     * Constructor
     *
     * @param baseMember   The member that this panel is built around
     * @param saveOnUpdate Should the action of the panel be done as soon as an update is received? or on command
     * @param master       The parent of this object to be notified if data changes
     */
    public IntractableObject_Currency(ExecutableMember<Double> baseMember, boolean saveOnUpdate, int order, Locale currencyLocale, Updatable master) {
        super(baseMember, saveOnUpdate, order, master);
        this.currencyLocale = currencyLocale;
    }

    /**
     * Set A user set source for the locale
     *
     * @param localeSource A user set source for the locale
     */
    public void setLocaleInspector(CurrencyDecoder_LocaleSource localeSource) {
        this.localeSource = localeSource;
        update();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setValue(Double value) {
        Locale locale = currencyLocale;
        if (localeSource != null) {
            locale = localeSource.getLocale(baseMember.getObject());
        }
        if (locale == null) {
            super.setValue(value);
            return;
        }

        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        value_txt.setText(format.format(value));
    }
}
