package org.skycastle.entity.accesscontrol

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
final case class RoleMembersFunction( isMember : EntityId => Boolean ) extends RoleMember {
  def containsEntity(entity: EntityId) = isMember( entity )
}

