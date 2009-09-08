package org.skycastle.entity.expression.bool



/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class Not( expression : BoolExpression ) extends BoolExpression {

  def getValue(entity: Properties) = getBoolean( entity )

  override def getNumber(entity: Properties) = Math.NaN_DOUBLE

  override def getBoolean(entity: Properties) = !expression.getBoolean( entity )


  override def toString = "not " + expression.toString
}