package org.skycastle.util


import java.io.Serializable

object Parameters {
  def apply (  elems: (Symbol, Any)*) = new Parameters( Map.empty ++ elems )
}

/**
 * A set of named properties.
 *
 * Mutable, but not a managed object.
 *
 * NOTE: All parameter contents should be serializable
 *
 * @author Hans Haggstrom
 */
// TODO: Convert to immutable, and have a separate mutable properties class?
@serializable
@SerialVersionUID( 1 )
final class Parameters(var properties_ : Map[Symbol, Any]) {

  def properties = properties_
  def entries = properties_

  def contains( id : Symbol ) : Boolean = properties.contains( id )
  
  def get[T](id: Symbol, defaultValue: T) : T = properties.getOrElse(id, defaultValue).asInstanceOf[T]

  def getAs[T](id: Symbol, defaultValue: T) : T = {
    val value = properties.getOrElse(id, defaultValue)
    if (value != null)
      value.asInstanceOf[T]
    else defaultValue
  }

  def getInt(id: Symbol, defaultValue: Int) : Int = {
    val value = properties.getOrElse(id, defaultValue)
    if (value == null) defaultValue
    else if (value.isInstanceOf[Number]) value.asInstanceOf[Number].intValue
    else defaultValue
  }

  def getFloat(id: Symbol, defaultValue: Float) : Float= {
    val value = properties.getOrElse(id, defaultValue)
    if (value == null) defaultValue
    else if (value.isInstanceOf[Number]) value.asInstanceOf[Number].floatValue
    else defaultValue
  }
  
  def getBoolean(id: Symbol, defaultValue: Boolean ) : Boolean = {
    val value = properties.getOrElse(id, defaultValue)
    if (value == null) defaultValue
    else if (value.isInstanceOf[Boolean]) value.asInstanceOf[Boolean]
    else defaultValue
  }

  def getString(id: Symbol, defaultValue: String) : String = {
    val s = properties.getOrElse(id, defaultValue)
    if (s == null) defaultValue else s.toString
  }

  def set(id: Symbol, value: Object) {
    val entry = (id, value)
    properties_ = properties_ + entry
  }

  def update( newValues : Parameters) {
    properties_ = properties_ ++ newValues.properties
  }

  override def toString = properties.mkString( "{", ", ", "}")

  override def equals(obj: Any) = {
    if ( obj == null ) false
    else if ( !classOf[Parameters].isInstance( obj ) ) false
    else properties_.equals( (obj.asInstanceOf[Parameters]).properties_ )
  }

  override def hashCode = super.hashCode ^ properties_.hashCode


}