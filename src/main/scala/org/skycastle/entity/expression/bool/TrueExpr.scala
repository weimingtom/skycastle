package org.skycastle.entity.expression.bool

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class TrueExpr() extends BoolExpression {
  def getBoolean(entity: Properties) = true

  override def toString = "true"
}