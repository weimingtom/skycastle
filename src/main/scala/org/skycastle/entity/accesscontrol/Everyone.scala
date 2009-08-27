package org.skycastle.entity.accesscontrol

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
case object Everyone extends RoleMember {
  def containsEntity(entity: EntityId) = true
}

