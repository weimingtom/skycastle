package org.skycastle.entity.expression.num



/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class Num( value : Number ) extends NumExpression {

  def getNumber(entity: Properties) = value.doubleValue

  override def toString = value.doubleValue.toString
}