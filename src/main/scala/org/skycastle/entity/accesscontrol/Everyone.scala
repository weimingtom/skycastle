package org.skycastle.entity.accesscontrol

import org.skycastle.entity.EntityId

/**
 * Represents all entities.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
case object Everyone extends RoleMember {
  def containsEntity(entity: EntityId) = true
}

