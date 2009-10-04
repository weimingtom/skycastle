package org.skycastle.entity.accesscontrol

import org.skycastle.entity.EntityId

/**
 * Something that can be granted a users.  Ultimately boils down to collections of EntityId:s
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
trait RoleMember {
  def containsEntity(entity: EntityId) : Boolean
}

