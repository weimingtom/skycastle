package org.skycastle.entity.expression.num


/**
* Represents a numerical expression in parenthesis.
*
* @author Hans Haggstrom
*/
final case class NumParen ( expression : NumExpression ) extends NumExpression {

  def getNumber(entity: Properties) = expression.getNumber( entity )

  override def toString = "( " + expression.toString + " )"
}