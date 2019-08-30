package com.ntankard.ClassExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DisplayProperties {

    enum Format {
        NONE,   // Format based on class
        AUD,    // AUD currency, field must be double otherwise this is ignored
        YEN     // YEN currency, field must be double otherwise this is ignored
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
     * Should the data be formatted in any special way?
     */
    Format format() default Format.NONE;
}
