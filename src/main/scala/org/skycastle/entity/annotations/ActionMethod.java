package org.skycastle.entity.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that indicates that a given method of an Entity is an Action that can be called by other entities or users.
 *
 * @author Hans Haggstrom
 */
@Retention( RetentionPolicy.RUNTIME)
@Target( ElementType.METHOD)
public @interface ActionMethod
{
    
}
