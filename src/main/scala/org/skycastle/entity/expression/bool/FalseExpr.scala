package org.skycastle.entity.expression.bool
import org.skycastle.util.Properties

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class FalseExpr() extends BoolExpression {
  def getBoolean(entity: Properties) = false
  override def toString = "false"
}