package org.skycastle.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that a method in an Entity is an action that can be called by another Entity through the action call mechanism.
 *
 * Specifies which incoming parameter names should be wrapped to which parameter, as a space separated string.
 *
 * @author Hans Haggstrom
 */
@Retention( RetentionPolicy.RUNTIME)
public @interface action
{
    /**
     * @return Space separated list of parameter names to use when calling the action method.
     */
    String value() default "";
}





