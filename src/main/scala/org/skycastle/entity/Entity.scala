package org.skycastle.entity


import _root_.org.skycastle.entity.entitycontainer.EntityContainer
import accesscontrol.{Role, RoleMember, Capability}
import java.util.logging.{Logger, Level}
import script.Script
import util.{LogMethods, Parameters}
/**
 * Represents some mutable object in the game (server or client).
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class Entity extends LogMethods {

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

  /**
   * The ID of the entity that is currently calling an action on this entity, or null if this entity is not processing
   * an action.
   *
   * Can be used e.g. to send back log messages to the calling entity if it is authorized and interested in receiving them.
   */
  @transient
  private var currentCaller : EntityId = null

  @transient
  private var currentAction : String = null

  // TODO: Maybe add RoleMember that is a check if caller id is in some collection in a property -> use some collections of entity id:s in properties as role members?
  // Complex cases could be e.g. Organization maintenance, handling different guild functions, etc.


  /**
   *  Update the properties of this Entity
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

    currentCaller = caller
    currentAction = actionId

    try {
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
                logWarning( "Caller '"+caller+"' tried to call action '"+actionId+"' on entity "+id+", but no such action found.  Ignoring call." )
              }
            }
        }
      }
      else {
        logWarning( "Caller '"+caller+"' is not authorized to call action '"+actionId+"' on entity "+id+".  Ignoring call." )
      }
    } catch {
       case e : Throwable => {
         logError( "Exception when '"+caller+"' called action '"+actionId+"' with parameters '"+parameters+"' on entity "+id+" : " + e, e )
       }
    }

    // TODO: Add these to default clause in try catch if possible
    currentAction = null
    currentCaller = null
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


  def logger : Logger = EntityLogger.logger

  def log( level : Level, message : => String , exception : => Throwable ) {
    if (logger != null && logger.isLoggable(level) )
    {
      val e = exception
      val prefixedMessage = "Entity " + id + " of type " + getClass.getName + ": " + message

      logger.log( level, prefixedMessage, e )

      // Send log feedback also to the caller, if it is authorized.  Useful for rapid debugging by developers and client scripters
      // Avoid infinite loops of error messages by not sending log messages if we are processing an incoming log message.
      if (currentCaller != null && currentAction != "callFeedback" ) {

        // Check if the caller is authorized to receive log messages by this entity
        getRole( "logListener" ) match {
          case Some( role : Role ) => {
            if ( role.containsEntity( currentCaller ) ) {

              // Invoke a log feedback action on the other entity asynchronously
              callOtherEntity( currentCaller, "callFeedback", Parameters(Map(
                'callingEntity -> currentCaller,
                'calledEntity -> id,
                'calledEntityType -> getClass.getName,
                'calledAction -> currentAction,
                'logLevel -> level.getName,
                'logMessage -> message,
                'exception -> e.getMessage )) )
              
            }
          }
          case None =>
        }
      }

      // TODO: Some entities could maybe also be added as general log listeners for this entity,
      // e.g. a developer instead of the caller could listen to all logs for an entity

    }
  }


  def callOtherEntity( targetEntityId : EntityId, actionName : String, parameters : Parameters ) {

    val callerId = id

    // TODO: Send call asyncronously

    null

  }
}


