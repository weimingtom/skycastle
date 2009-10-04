package org.skycastle.entity.expression.bool
import org.skycastle.util.PropertyGetters

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class TrueExpr() extends BoolExpression {
  def getBoolean(entity: PropertyGetters) = true

  override def toString = "true"
}