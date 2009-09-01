package org.skycastle.entity


import _root_.org.skycastle.entity.entitycontainer.EntityContainer
import accesscontrol.{Role, RoleMember, Capability}
import script.Script
import util.Parameters

/**
 * Represents some mutable object in the game (server or client).
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class Entity {

  /**
   * Identifier of this Entity.
   * Initialized when the entity is added to an EntityContainer.
   * (TODO: Can we use scala to indicate that it should only be changed by the entity container somehow?  E.g. private[entitycontainer]?)
   */
  var id: EntityId = null

  /**
   * Used for accessing operations involving the system that keeps track and stores all entities.
   * Initialized when the Entity is added to an EntityContainer.
   * (TODO: Can we use scala to indicate that it should only be changed by the entity container somehow?  E.g. private[entitycontainer]?)
   */
  var container : EntityContainer = null

  /**
   * Key-value properties stored in this Entity
   */
  val properties = new Parameters()

  /**
   * Any scripted actions added to this Entity
   */
  private val dynamicActions: Map[String, Script] = Map()

  /**
   * The roles for role based security access control to the actions of this Entity.
   */
  private var roles : List[Role] = Nil

  // TODO: Maybe add RoleMember that is a check if caller id is in some collection in a property -> use some collections of entity id:s in properties as role members?
  // Complex cases could be e.g. Organization maintenance, handling different guild functions, etc.

  
  /**
   * Update the properties of this Entity
   */
  def updateProperties(updatedProperties: Parameters) {
    properties.update(updatedProperties)
  }

  def getRoles : List[Role] = roles

  def getRole( roleId : String ) : Option[Role] = roles.find( _.roleId == roleId )

  def addRole( roleId : String ) {
    // TODO: Check role id syntax?  No special chars, java style identifier?
    if (roleId != null) {
      getRole(roleId) match {
        case Some(role) => // TODO: Overlap, can not add.  Some error?  Or just a log message?
        case None => roles = roles ::: List( new Role( roleId ) )
      }
    }
  }

  def removeRole( roleId : String ) {
    roles = roles.remove( _.roleId == roleId )
  }

  def addRoleMember( roleId : String, member : RoleMember ) {
    getRole(roleId) match {
      case Some(role:Role) => role.addMember( member )
      case None => // TODO: Logg warning?
    }
  }

  def removeRoleMember( roleId : String, member : RoleMember ) {
    getRole(roleId) match {
      case Some(role:Role) => role.removeMember( member )
      case None => // TODO: Logg warning?
    }
  }

  def addRoleCapability( roleId : String, capability : Capability ) {
    getRole(roleId) match {
      case Some(role:Role) => role.addCapability( capability )
      case None => // TODO: Logg warning?
    }
  }

  def removeRoleCapability( roleId : String, capability : Capability ) {
    getRole(roleId) match {
      case Some(role:Role) => role.removeCapability( capability )
      case None => // TODO: Logg warning?
    }
  }

  // Special actions:
  // * Remove self
  // * Add / remove / change action


  /**
   * Call an action available in this entity.
   */
  def call(caller: EntityId, actionId: String, parameters: Parameters) {

    // Check access rights.  By default allow any call by this entity itself.
    // (the identity or privilegies of original caller are not retained when an action invokes another action,
    // instead the identity of the entity that contains the calling action is used.)
    if ( caller == id || roles.exists( _.allowsCall( caller, actionId ) )) {
      // Try to handle with default entity actions
      if (!callDefaultAction(actionId, parameters)) {
        // Try to handle with builtin actions from inheriting classes
        if (!callBuiltinAction(actionId, parameters)) {
            // Try to handle with dynamic actions
            val action: Script = dynamicActions.getOrElse(actionId, null)
            if (action != null) {
              action.run( this, parameters)
            }
            else {
              // TODO: Logg action not found
            }
          }
      }
    }
    else {
      // TODO: Logg action not allowed
    }
  }

  /**
   * Allows for use of a simple switch clause to invoke any custom actions provided by decendant Entities.
   * Return true if the action was handled, false if not.
   */
  protected def callBuiltinAction(actionName: String, parameters: Parameters): Boolean = { false }

  /**
   * Handles default actions provided for all entities.
   * Return true if the action was handled, false if not.
   */
  private def callDefaultAction(actionName: String, parameters: Parameters): Boolean = {
    actionName match {
      case "addRole" => addRole( parameters.getAs[String]('roleId, null )  ) ; true
      case "removeRole" => removeRole( parameters.getAs[String]('roleId, null )  ) ; true
      // TODO: The rest
      case _ => false
    }
  }

  /* * TODO: This is somewhat special case, maybe could be removed from Entity?
   * Creates an user interface for viewing / invoking actions of this Entity.
   * The parameters can provide additional configuration information for the UI.
   */
  //  def createUi( parameters : Parameters ) : Ui

}


