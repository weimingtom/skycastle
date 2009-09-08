package org.skycastle.entity.expression

/**
 * 
 * 
 * @author Hans Haggstrom
 */

case class Prop( propertyName : Symbol, default : Expression ) extends Expression {

  def getValue(entity: Properties) = {
    entity.get( propertyName ) match {
      case Some(x) => x
      case None => default.getValue( entity )
    }
  }

  override def toString = propertyName.name + "[default: "+default.toString+" ]"

}