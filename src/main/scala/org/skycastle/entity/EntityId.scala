package org.skycastle.entity


import accesscontrol.RoleMember

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class EntityId( id : String ) extends RoleMember {

  if (id == null) throw new IllegalArgumentException( "The id must not be null" )

  def managedObjectName = id
  override def toString = managedObjectName
  
  def containsEntity(entity: EntityId) = entity.id == id
}

