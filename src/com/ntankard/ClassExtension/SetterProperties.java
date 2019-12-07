package com.ntankard.ClassExtension;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SetterProperties {

    /**
     * The name of the method on a source object that can return a list of data that can be used to set this field
     *
     * @return The name of the method on a source object that can return an array (or map) of data that can be used to set this field
     */
    String localSourceMethod();

    /**
     * Should the field be able to be set by the display?
     *
     * @return True if the field be able to be set by the display?
     */
    boolean displaySet() default true;
}
