package org.skycastle.util

import _root_.org.skycastle.entity.entitycontainer.EntityContainer

object Properties {

  def apply( elements: (Symbol, Any)* ) : Properties = new Properties( Map.empty ++ elements )

}


/**
 * A class that provides mutable Property support.
 * 
 * @author Hans Haggstrom
 */
case class Properties() extends TypedGetters {

  def this( parameters : Parameters ) {
    this()
    properties = parameters.entries
  }

  def this( elements : Map[Symbol, Any] ) {
    this()
    properties = elements
  }

  def this( elements: (Symbol, Any)* ) {
    this()
    properties = Map.empty ++ elements
  }

  private var properties : Map[Symbol, Any] = Map()

  def entries : Map[Symbol, Any] = properties

  def setProperties( newProperties : Parameters ) {
    properties = newProperties.entries
  }

  def updateProperties( changed : Parameters ) {
    properties = properties ++ changed.entries
  }

  def getProperties : Map[Symbol,Any] = properties

  def set( property : Symbol, value : Any ) {
    val entry = (property, value)
    properties = properties + entry
  }

  def toParameters : Parameters = {
    new Parameters( properties )
  }


}

