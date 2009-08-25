package org.skycastle.util

/**
 * Holds user readable information for some object, such as name, description, icon reference.
 * Internationalization support could be added later.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class Description( name : String, description : String ) {
  override def toString = name
}

