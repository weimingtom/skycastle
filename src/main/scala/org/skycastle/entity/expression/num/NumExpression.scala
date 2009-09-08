package org.skycastle.entity.expression.num


import bool.{Smaller, Greater, SmallerOrEqual, GreaterOrEqual}
import org.skycastle.util.Properties

/**
 * 
 * 
 * @author Hans Haggstrom
 */

trait NumExpression extends Expression {


  def getValue(entity: Properties) = getNumber( entity )


  /**
   * The numerical value of this entity, mostly for use in triggers and such that do
   * simple calculations with an entity and compares properties.
   *
   * Will be Math.NAN_DOUBLE if the value is not a number.
   */
  def getNumber( entity : Properties  ) : Double


  final def + ( expression : NumExpression ) = Add( this, expression )
  final def - ( expression : NumExpression ) = Sub( this, expression )
  final def / ( expression : NumExpression ) = Div( this, expression )
  final def * ( expression : NumExpression ) = Mul( this, expression )

  final def < ( expression : NumExpression ) = Smaller( this, expression )
  final def > ( expression : NumExpression ) = Greater( this, expression )
  final def <= ( expression : NumExpression ) = SmallerOrEqual( this, expression )
  final def >= ( expression : NumExpression ) = GreaterOrEqual( this, expression )


}


final case class Add( exp1 : NumExpression, exp2 : NumExpression ) extends NumExpression with BinaryExpression {
  val symbol = "+"
  override def getNumber(entity: Properties) = exp1.getNumber( entity ) + exp2.getNumber( entity )
}

final case class Sub( exp1 : NumExpression, exp2 : NumExpression ) extends NumExpression with BinaryExpression {
  val symbol = "-"
  override def getNumber(entity: Properties) = exp1.getNumber( entity ) - exp2.getNumber( entity )
}

final case class Mul( exp1 : NumExpression, exp2 : NumExpression ) extends NumExpression with BinaryExpression {
  val symbol = "*"
  override def getNumber(entity: Properties) = exp1.getNumber( entity ) * exp2.getNumber( entity )
}

final case class Div( exp1 : NumExpression, exp2 : NumExpression ) extends NumExpression with BinaryExpression {
  val symbol = "/"
  override def getNumber(entity: Properties) = exp1.getNumber( entity ) / exp2.getNumber( entity )
}



