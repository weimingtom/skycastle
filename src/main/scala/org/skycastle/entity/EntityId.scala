package org.skycastle.entity

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class EntityId( id : String ) {
  def managedObjectName = id
  override def toString = managedObjectName

}

