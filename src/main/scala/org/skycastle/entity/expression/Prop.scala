package org.skycastle.entity.expression

import org.skycastle.util.{PropertyGetters}

/**
 * 
 * 
 * @author Hans Haggstrom
 */

case class Prop( propertyName : Symbol, default : Expression ) extends Expression {

  def getValue(entity: PropertyGetters) = {
    entity.getProperty( propertyName ) match {
      case Some(x) => x
      case None => default.getValue( entity )
    }
  }

  override def toString = propertyName.name + "[default: "+default.toString+" ]"

}