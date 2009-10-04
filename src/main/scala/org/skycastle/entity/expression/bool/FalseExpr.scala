package org.skycastle.entity.expression.bool
import org.skycastle.util.PropertyGetters

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class FalseExpr() extends BoolExpression {
  def getBoolean(entity: PropertyGetters) = false
  override def toString = "false"
}