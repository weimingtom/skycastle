package org.skycastle.entity.accesscontrol

import org.skycastle.entity.EntityId


/**
 * Used in users based access control to Entity Actions.
 *
 * Has some members that are granted some rights.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
final case class Role( roleId : Symbol ) {

  private var members : List[RoleMember] = Nil
  private var capabilities : List[Right] = Nil

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


  def getRights = capabilities
  def addRight( right : Right ) {
    if (right != null && !capabilities.contains(right) )
      capabilities = right :: capabilities
  }
  def removeRight( right : Right ) {
    if (right != null)
      capabilities = capabilities.remove( _ == right )
  }

  def canCall( actionId : Symbol ) : Boolean = capabilities.exists( _.allowsCall( actionId ) )
  def canRead( property : Symbol ) : Boolean = capabilities.exists( _.allowsRead( property ) )
  def canWrite( property : Symbol ) : Boolean = capabilities.exists( _.allowsWrite( property ) )

  def canCall( caller : EntityId, actionId : Symbol ) : Boolean = containsEntity( caller ) && canCall( actionId )
  def canRead( caller : EntityId, property : Symbol ) : Boolean = containsEntity( caller ) && canRead( property )
  def canWrite( caller : EntityId, property : Symbol ) : Boolean = containsEntity( caller ) && canWrite( property )


  override def toString = "Role '"+roleId+"', members: "+members.mkString("  ")+", capabilities: "+capabilities.mkString("  ") +"."
}

