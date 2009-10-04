package org.skycastle.entity.expression.bool
import org.skycastle.util.PropertyGetters



/**
 * 
 * 
 * @author Hans Haggstrom
 */

class BoolProp(propertyName : Symbol, default : BoolExpression)  extends BoolExpression {

  def getBoolean (entity: PropertyGetters) = {
    entity.getProperty( propertyName ) match {
      case Some(value) => {
        if ( value.isInstanceOf[Boolean] ) value.asInstanceOf[Boolean]
        else if ( value.isInstanceOf[java.lang.Boolean] ) value.asInstanceOf[java.lang.Boolean].booleanValue
        else default.getBoolean( entity )
      }
      case None => default.getBoolean( entity )
    }
  }

  override def toString = propertyName.name + "[default: "+default.toString+" ]"

}