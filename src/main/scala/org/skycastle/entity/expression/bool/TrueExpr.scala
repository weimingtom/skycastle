package org.skycastle.entity.expression.bool

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class TrueExpr extends Expression {
  def getValue(entity: Properties) = true
  override def getBoolean(entity: Properties) = true
  override def getNumber(entity: Properties) = Math.NaN_DOUBLE

  override def toString = "true"
}