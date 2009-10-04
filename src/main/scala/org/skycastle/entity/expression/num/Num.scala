package org.skycastle.entity.expression.num
import org.skycastle.util.PropertyGetters



/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class Num( value : Number ) extends NumExpression {

  def getNumber(entity: PropertyGetters) = value.doubleValue

  override def toString = value.doubleValue.toString
}