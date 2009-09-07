package org.skycastle.entity.expression.bool

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class FalseExpr extends Expression {
  def getValue(entity: Properties) = false
  override def getBoolean(entity: Properties) = false
  override def getNumber(entity: Properties) = Math.NaN_DOUBLE
  override def toString = "false"
}