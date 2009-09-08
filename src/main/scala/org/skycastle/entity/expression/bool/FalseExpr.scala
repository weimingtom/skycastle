package org.skycastle.entity.expression.bool

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class FalseExpr() extends BoolExpression {
  def getBoolean(entity: Properties) = false
  override def toString = "false"
}