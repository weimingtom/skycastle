package org.skycastle.entity.expression.num
import org.skycastle.util.PropertyGetters


/**
* Represents a numerical expression in parenthesis.
*
* @author Hans Haggstrom
*/
final case class NumParen ( expression : NumExpression ) extends NumExpression {

  def getNumber(entity: PropertyGetters) = expression.getNumber( entity )

  override def toString = "( " + expression.toString + " )"
}