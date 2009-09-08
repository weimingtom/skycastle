package org.skycastle.entity.expression
import org.skycastle.util.Properties

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class Const( value : Any ) extends Expression {

  def getValue(entity: Properties) = value

  override def toString = value.toString
}