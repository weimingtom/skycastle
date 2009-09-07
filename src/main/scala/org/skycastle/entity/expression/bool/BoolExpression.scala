package org.skycastle.entity.expression.bool

/**
 * 
 * 
 * @author Hans Haggstrom
 */

trait  BoolExpression extends Expression {


  def getValue(entity: Properties) = getBoolean( entity )

  /**
   * The boolean value of this entity, mostly for use in triggers and such that do
   * simple calculations with an entity and compares properties.
   *
   * Will be false if the value is not a boolean.
   */
  def getBoolean( entity : Properties  ) : Boolean

  final def and ( expression : BoolExpression ) = And( this, expression )
  final def or ( expression : BoolExpression ) = Or( this, expression )
  final def xor ( expression : BoolExpression ) = Xor( this, expression )

  final def not = Not( this )


}


final case class And( exp1 : Expression, exp2 : Expression ) extends BoolExpression with BinaryExpression {
  val symbol = "and"
  override def getBoolean(entity: Properties) = exp1.getBoolean( entity ) && exp2.getBoolean( entity )
}

final case class Or( exp1 : Expression, exp2 : Expression ) extends BoolExpression with BinaryExpression {
  val symbol = "or"
  override def getBoolean(entity: Properties) = exp1.getBoolean( entity ) || exp2.getBoolean( entity )
}

final case class Xor( exp1 : Expression, exp2 : Expression ) extends BoolExpression with BinaryExpression {
  val symbol = "xor"
  override def getBoolean(entity: Properties) = exp1.getBoolean( entity ) ^^ exp2.getBoolean( entity )
}


final case class SameAs( exp1 : Expression, exp2 : Expression ) extends BoolExpression with BinaryExpression {
  val symbol = "=="
  override def getBoolean(entity: Properties) = exp1.getValue( entity ) == exp2.getValue( entity )
}

final case class NotSameAs( exp1 : Expression, exp2 : Expression ) extends BoolExpression with BinaryExpression {
  val symbol = "!="
  override def getBoolean(entity: Properties) = exp1.getValue( entity ) != exp2.getValue( entity )
}


final case class Smaller( exp1 : Expression, exp2 : Expression ) extends BoolExpression with BinaryExpression {
  val symbol = "<"
  override def getBoolean(entity: Properties) = exp1.getNumber( entity ) < exp2.getNumber( entity )
}

final case class SmallerOrEqual( exp1 : Expression, exp2 : Expression ) extends BoolExpression with BinaryExpression {
  val symbol = "<="
  override def getBoolean(entity: Properties) = exp1.getNumber( entity ) <= exp2.getNumber( entity )
}

final case class Greater( exp1 : Expression, exp2 : Expression ) extends BoolExpression with BinaryExpression {
  val symbol = ">"
  override def getBoolean(entity: Properties) = exp1.getNumber( entity ) > exp2.getNumber( entity )
}

final case class GreaterOrEqual( exp1 : Expression, exp2 : Expression ) extends BoolExpression with BinaryExpression {
  val symbol = ">="
  override def getBoolean(entity: Properties) = exp1.getNumber( entity ) >= exp2.getNumber( entity )
}



