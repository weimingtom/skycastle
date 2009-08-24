package org.skycastle.util


import java.io.Serializable

/**
 * A set of named properties.
 *
 * Mutable, but not a managed object.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class Properties(var properties: Map[Symbol, Serializable]) {

  def this() = this( Map() )

  def get(id: Symbol, defaultValue: Serializable) = properties.getOrElse(id, defaultValue)

  def getInt(id: Symbol, defaultValue: Int) : Int = {
    val value = properties.getOrElse(id, defaultValue)
    if (value.isInstanceOf[Number]) value.asInstanceOf[Number].intValue
    else defaultValue
  }

  def getFloat(id: Symbol, defaultValue: Float) : Float= {
    val value = properties.getOrElse(id, defaultValue)
    if (value.isInstanceOf[Number]) value.asInstanceOf[Number].floatValue
    else defaultValue
  }
  
  def getBoolean(id: Symbol, defaultValue: Boolean ) : Boolean = {
    val value = properties.getOrElse(id, defaultValue)
    if (value.isInstanceOf[Boolean]) value.asInstanceOf[Boolean]
    else defaultValue
  }

  def getString(id: Symbol, defaultValue: String) : String = {
    properties.getOrElse(id, defaultValue).toString
  }

  def set(id: Symbol, value: Serializable) {
    val entry = (id, value)
    properties = properties + entry
  }

  def update( newValues : Properties) {
    properties = properties ++ newValues.properties 
  }
}