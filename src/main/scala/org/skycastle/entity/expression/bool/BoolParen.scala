package org.skycastle.entity.expression.bool



import org.skycastle.util.PropertyGetters

/**
* Represents a boolean expression in parenthesis.
*
* @author Hans Haggstrom
*/
final case class BoolParen ( expression : BoolExpression ) extends BoolExpression {

  def getBoolean(entity: PropertyGetters) = expression.getBoolean( entity )

  override def toString = "( " + expression.toString + " )"
}

