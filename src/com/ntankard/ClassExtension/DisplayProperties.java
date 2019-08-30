package com.ntankard.ClassExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayProperties {

    enum DataType {
        AS_CLASS,       // DataType based on class
        CURRENCY_AUD,   // AUD currency, field must be double otherwise this is ignored
        CURRENCY_YEN,   // YEN currency, field must be double otherwise this is ignored
    }

    enum DataContext {
        NONE,           // Data values have no specific value
        ZERO_BELOW_BAD, // Data above zero is normal but anything below 0 is noteworthy
        ZERO_BINARY,    // Data is centered on zero, values above or below are note worthy
    }

    /**
     * What name should be displayed?
     */
    String name() default "";

    /**
     * If multiple members from a class are being displayed at once, what order should they be in?
     */
    int order() default Integer.MAX_VALUE;

    /**
     * Should the data be interpreted as something other than just its class
     */
    DataType dataType() default DataType.AS_CLASS;

    /**
     * Can the value provide any context to the state of the data (not used to field verification)
     */
    DataContext dataContext() default DataContext.NONE;
}
