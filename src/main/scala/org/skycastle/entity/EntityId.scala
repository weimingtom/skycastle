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

  def managedObjectName = id
  override def toString = managedObjectName
  
  def containsEntity(entity: EntityId) = entity.id == id
}

