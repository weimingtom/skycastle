package org.skycastle.entity.accesscontrol

/**
 * Something that can be granted a role.  Ultimately boils down to collections of EntityId:s
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
trait RoleMember {
  def containsEntity(entity: EntityId) : Boolean
}

