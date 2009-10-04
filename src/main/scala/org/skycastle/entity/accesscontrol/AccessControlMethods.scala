package org.skycastle.entity.accesscontrol

import org.skycastle.util.Parameters
import org.skycastle.util.ParameterChecker._
import org.skycastle.entity.{CallException, EntityId, parameters}

/**
 * Encapsulates the concept of access control to actions and properties in an Entity.
 * 
 * @author Hans Häggström
 */
// TODO: More compact representation for the access rights.
// Capabilities are probably added less than new Member types,
// so we could hide the Right object from outside view and just provide the needed methods..?
// That way it is easier to optimize the used structures internally, and the API comes simpler too.
// TODO: Rename Right to Right
trait AccessControlMethods {

  /**
   * The roles for users based security access control to the actions of this Entity.
   */
  private var roles : List[Role] = Nil


  def getRoles : List[Role] = roles

  def getRole( roleId : Symbol ) : Option[Role] = roles.find( _.roleId == roleId )

  def hasRole( roleId : Symbol ) : Boolean = roles.exists( _.roleId == roleId )


  @users( "roleEditor"  )
  @parameters( "roleId"  )
  def addRole( roleId : Symbol ) : Role = {
    requireIsIdentifier(roleId, 'roleId)

    getRole(roleId) match {
      case Some(role) => throwWarning( "Can not add role '"+roleId+"', it already exists." )
      case None => {
        val role = new Role( roleId )
        roles = roles ::: List( role )
        role
      }
    }
  }

  def addRoleWithMembers( roleId : Symbol, members : RoleMember * ) : Role = {
    val role = addRole( roleId )

    if (hasRole(roleId)) members foreach { addRoleMember( roleId, _ ) }
    role
  }


  @users( "roleEditor"  )
  @parameters( "roleId"  )
  def removeRole( roleId : Symbol ) {
    requireNotNull(roleId, 'roleId)

    roles = roles.remove( _.roleId == roleId )
  }

  @users( "roleEditor"  )
  @parameters( "roleId, member"  )
  def addRoleMember( roleId : Symbol, member : RoleMember ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(member, 'member)

    getRole(roleId) match {
      case Some(role:Role) => role.addMember( member )
      case None => throwWarning( "Can not add '"+member+"' to role '"+roleId+"', no such role found." )
    }
  }

  @users( "roleEditor"  )
  @parameters( "roleId, member" )
  def removeRoleMember( roleId : Symbol, member : RoleMember ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(member, 'member)

    getRole(roleId) match {
      case Some(role:Role) => role.removeMember( member )
      case None => throwWarning( "Can not remove '"+member+"' from role '"+roleId+"', no such role found." )
    }
  }

  @users( "roleEditor"  )
  @parameters( "roleId, actionId"  )
  def addCallRight( roleId : Symbol, allowedAction : Symbol ) {
    addRoleCapability( roleId, CallRight( allowedAction ) )
  }

  @users( "roleEditor"  )
  @parameters( "roleId, actionId"  )
  def removeCallRight( roleId : Symbol, allowedAction : Symbol ) {
    removeRoleCapability( roleId, CallRight( allowedAction ) )
  }

  @users( "roleEditor"  )
  @parameters( "roleId, propertyId"  )
  def addEditRight( roleId : Symbol, propertyId : Symbol ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(propertyId, 'propertyId)

    addRoleCapability( roleId, EditRight( propertyId ) )
  }

  @users( "roleEditor"  )
  @parameters( "roleId, propertyId"  )
  def removeEditRight( roleId : Symbol, propertyId : Symbol ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(propertyId, 'propertyId)

    removeRoleCapability( roleId, EditRight( propertyId ) )
  }

  @users( "roleEditor"  )
  @parameters( "roleId, propertyId"  )
  def addReadRight( roleId : Symbol, propertyId : Symbol ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(propertyId, 'propertyId)

    addRoleCapability( roleId, ReadRight( propertyId ) )
  }

  @users( "roleEditor"  )
  @parameters( "roleId, propertyId"  )
  def removeReadRight( roleId : Symbol, propertyId : Symbol ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(propertyId, 'propertyId)

    removeRoleCapability( roleId, ReadRight( propertyId ) )
  }

  @users( "roleEditor"  )
  @parameters( "roleId, propertyId"  )
  def addWriteRight( roleId : Symbol, propertyId : Symbol ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(propertyId, 'propertyId)

    addRoleCapability( roleId, WriteRight( propertyId ) )
  }

  @users( "roleEditor"  )
  @parameters( "roleId, propertyId"  )
  def removeWriteRight( roleId : Symbol, propertyId : Symbol ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(propertyId, 'propertyId)

    removeRoleCapability( roleId, WriteRight( propertyId ) )
  }

  def addRoleCapability( roleId : Symbol, capability : Right ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(capability, 'capability)

    getRole(roleId) match {
      case Some(role:Role) => role.addRight( capability )
      case None => throwWarning( "Can not add capability '"+capability+"' to role '"+roleId+"', no such role found." )
    }
  }

  def removeRoleCapability( roleId : Symbol, capability : Right ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(capability, 'capability)

    getRole(roleId) match {
      case Some(role:Role) => role.removeRight( capability )
      case None => throwWarning( "Can not remove capability '"+capability+"' from role '"+roleId+"', no such role found." )
    }
  }

  protected def callAllowed( caller: EntityId, called :EntityId, actionId: Symbol, parameters : Parameters  ) : Boolean = {

    def callerIsSelf : Boolean = caller == called
    def actionCallAllowed : Boolean = roles.exists( _.allowsCall( caller, actionId ) )
    def propertyMethodAllowed( method : Symbol, predicate : (Role, Symbol) => Boolean ) : Boolean = {
      actionId == method &&
      ( parameters.getProperty( 'property ) match {
        case Some(property : Symbol) => roles.exists( { predicate( _, property) } )
        case _ => false
      } )
    }

    // Check access rights.  By default allow any call by this entity itself.
    // (the identity or privileges of original caller are not retained when an action invokes another action,
    // instead the identity of the entity that contains the calling action is used.)
    callerIsSelf ||
      actionCallAllowed ||
      propertyMethodAllowed('getProperty, { _.allowsRead( caller, _ ) } ) ||
      propertyMethodAllowed('setProperty, { _.allowsWrite( caller, _ ) } )
  }



  private def throwWarning( message : String ) : Nothing = {
    throw CallException( message )
  }

}

