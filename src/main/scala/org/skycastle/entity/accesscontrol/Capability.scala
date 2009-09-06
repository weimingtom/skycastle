package org.skycastle.entity.accesscontrol

/**
 * 
 * 
 * @author Hans Haggstrom
 */

abstract class Capability {
  def allowsCall( action : Symbol ) : Boolean
  def allowsRoleMemberManagement( role : String ) : Boolean
}

/**
 * The capability to call a specific action on an Entity.
 */
case class ActionCapability( actionId : Symbol ) extends Capability {
  def allowsCall(action: Symbol) = action == actionId
  def allowsRoleMemberManagement(role: String) = false
}

/**
 * The capability to call any action on the entity.
 */
case object AllActionCapability extends Capability {
  def allowsCall(action: Symbol) = true
  def allowsRoleMemberManagement(role: String) = false
}

/* not used, instead create actions for adding people to specific roles (e.g. invite guild member, kick guild member, etc - they anyway will need possibility for different roles that can add and remove them.
*/
/**
 * The capability to add and remove members of a specific Role in an Entity.
 */
/*
case class RoleMemberManagementCapability( roleId : String ) extends Capability {
  def allowsCall(action: String) = false
  def allowsRoleMemberManagement( role : String ) = role == roleId
}

*/
/**
 * The capability to manage all role memberships of an entity.
 */
/*
case class AllRoleMeberManagementCapability extends Capability {
  def allowsCall(action: String) = false
  def allowsRoleMemberManagement(role: String) = true
}
*/

