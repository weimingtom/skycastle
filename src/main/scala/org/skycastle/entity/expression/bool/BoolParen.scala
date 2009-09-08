package org.skycastle.entity.expression.bool




/**
* Represents a boolean expression in parenthesis.
*
* @author Hans Haggstrom
*/
final case class BoolParen ( expression : BoolExpression ) extends BoolExpression {

  def getBoolean(entity: Properties) = expression.getBoolean( entity )

  override def toString = "( " + expression.toString + " )"
}

