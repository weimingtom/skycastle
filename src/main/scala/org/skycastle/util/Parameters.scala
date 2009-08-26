package org.skycastle.util


import java.io.Serializable

/**
 * A set of named properties.
 *
 * Mutable, but not a managed object.
 *
 * @author Hans Haggstrom
 */
@SerialVersionUID( 1 )
case class Parameters(var properties: Map[Symbol, Serializable]) extends Serializable{

  def this() = this( Map() )

  def contains( id : Symbol ) : Boolean = properties.contains( id )
  
  def get[T](id: Symbol, defaultValue: T) : T = properties.getOrElse(id, defaultValue).asInstanceOf[T]

  def getAs[T](id: Symbol, defaultValue: T) : T = {
    val value = properties.getOrElse(id, defaultValue)
    if (value != null && value.isInstanceOf[T])
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

  def set(id: Symbol, value: Serializable) {
    val entry = (id, value)
    properties = properties + entry
  }

  def update( newValues : Parameters) {
    properties = properties ++ newValues.properties 
  }

  override def toString = properties.mkString( "{", ", ", "}")
}