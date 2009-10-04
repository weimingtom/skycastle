package org.skycastle.entity.expression

import org.skycastle.util.{PropertyGetters}

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class Const( value : Any ) extends Expression {

  def getValue(entity: PropertyGetters) = value

  override def toString = value.toString
}