package org.skycastle.entity.accesscontrol



/**
 * Used in role based access control to Entity Actions.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
final case class Role( roleId : String ) {

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

  def allowsCall( caller : EntityId, actionId : Symbol ) : Boolean = {
    containsEntity( caller ) && hasCallCapability( actionId )
  }

}

