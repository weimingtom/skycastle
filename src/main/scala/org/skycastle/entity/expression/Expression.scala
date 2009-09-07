package org.skycastle.entity.expression


import bool.{NotSameAs, SameAs}

/**
 * Used to calculate some (usually simple) expression that is based on constants and properties of an Entity.
 * 
 * @author Hans Haggstrom
 */
// TODO: Do simple parsing instead of 
trait Expression {

  def getValue( entity : Properties ) : Any

  final def != ( expression : Expression ) = NotSameAs( this, expression )
  final def == ( expression : Expression ) = SameAs( this, expression )

}

