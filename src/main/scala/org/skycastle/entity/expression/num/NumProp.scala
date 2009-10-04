package org.skycastle.entity.expression.num
import org.skycastle.util.PropertyGetters

/**
 * 
 * 
 * @author Hans Haggstrom
 */

final case class NumProp(propertyName : Symbol, default : NumExpression) extends NumExpression {

  def getNumber (entity: PropertyGetters) = {
    entity.getProperty( propertyName ) match {
      case Some(value) => {
        if ( value.isInstanceOf[Number] ) value.asInstanceOf[Number].doubleValue
        else if ( value.isInstanceOf[Byte] ) value.asInstanceOf[Byte].toDouble
        else if ( value.isInstanceOf[Short] ) value.asInstanceOf[Short].toDouble
        else if ( value.isInstanceOf[Int] ) value.asInstanceOf[Int].toDouble
        else if ( value.isInstanceOf[Long] ) value.asInstanceOf[Long].toDouble
        else if ( value.isInstanceOf[Float] ) value.asInstanceOf[Float].toDouble
        else if ( value.isInstanceOf[Double] ) value.asInstanceOf[Double]
        else default.getNumber( entity )
      }
      case None => default.getNumber( entity )
    }
  }

  override def toString = propertyName.name + "[default: "+default.toString+" ]"


}