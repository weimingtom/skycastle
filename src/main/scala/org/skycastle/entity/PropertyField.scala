package org.skycastle.entity


import java.lang.reflect.Field

/**
 * Utility class for holding information discovered about fields in an Entity.
 */
case class PropertyField(host : Entity, field : Field ) {

  def getValue : Any = field.get( host )

  def setValue( value : Any ) : Unit = field.set( host, value )

}