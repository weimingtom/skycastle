package org.skycastle.entity.expression.bool

import org.skycastle.util.PropertyGetters


/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class Not( expression : BoolExpression ) extends BoolExpression {

  def getBoolean(entity: PropertyGetters) = !expression.getBoolean( entity )


  override def toString = "not " + expression.toString
}