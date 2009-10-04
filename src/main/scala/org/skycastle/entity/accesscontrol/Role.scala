package org.skycastle.entity.accesscontrol

import org.skycastle.entity.EntityId


/**
 * Used in users based access control to Entity Actions.
 * 
 * @author Hans Haggstrom
 */
// TODO: Refactor so that users just call add/remove member, add/removeCallCapability(action),
// add/remove edit/read/write capability for (property), and corresponding getters.
// That way it is easier to optimize the used structures internally, and the API comes simpler too.
@serializable
@SerialVersionUID(1)
final case class Role( roleId : Symbol ) {

  private var members : List[RoleMember] = Nil
  private var capabilities : List[Capability] = Nil

  def getMembers = members
  def addMember( member : RoleMember ) {
    if (member != null && !members.contains(member))
      members = member :: members
  }
  def removeMember( member : RoleMember ) {
    if (member != null) 
      members = members.remove(_ == member)
  }

  def containsEntity( entityId : EntityId ) : Boolean = members.exists(_.containsEntity(entityId))


  def getCapabilities = capabilities
  def addCapability( capability : Capability ) {
    if (capability != null && !capabilities.contains(capability) )
      capabilities = capability :: capabilities
  }
  def removeCapability( capability : Capability ) {
    if (capability != null)
      capabilities = capabilities.remove( _ == capability )
  }

  def hasCallCapability( actionId : Symbol ) : Boolean = capabilities.exists( _.allowsCall( actionId ) )
  def hasReadCapability( property : Symbol ) : Boolean = capabilities.exists( _.allowsRead( property ) )
  def hasWriteCapability( property : Symbol ) : Boolean = capabilities.exists( _.allowsWrite( property ) )

  def allowsCall( caller : EntityId, actionId : Symbol ) = containsEntity( caller ) && hasCallCapability( actionId )
  def allowsRead( caller : EntityId, property : Symbol ) = containsEntity( caller ) && hasReadCapability( property )
  def allowsWrite( caller : EntityId, property : Symbol ) = containsEntity( caller ) && hasWriteCapability( property )


  override def toString = "Role '"+roleId+"', members: "+members.mkString("  ")+", capabilities: "+capabilities.mkString("  ") +"."
}

