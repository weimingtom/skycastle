package org.skycastle.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method in an Entity is an action that can be called by another Entity through the action call mechanism.
 *
 * Specifies which incoming parameter names should be wrapped to which parameter, as a comma separated string.
 *
 * @author Hans Haggstrom
 */
@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.METHOD )
public @interface parameters
{
    /**
     * @return Space separated list of parameter names to use when calling the action method.
     */
    String value() default "";
}





