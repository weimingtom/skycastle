package org.skycastle.entity.expression


import bool.{NotSameAs, SameAs}

/**
 * Used to calculate some (usually simple) expression that is based on constants and properties of an Entity.
 * 
 * @author Hans Haggstrom
 */
// TODO: Add implicit conversion from numbers to Num
// TODO: Do simple parsing of expressions in strings instead of sending expression hierarchies over the net?
//       more efficient network transfer, but may eat some CPU cycles  
trait Expression {

  def getValue( entity : Properties ) : Any

  final def notSameAs ( expression : Expression ) = NotSameAs( this, expression )
  final def sameAs ( expression : Expression ) = SameAs( this, expression )

}

