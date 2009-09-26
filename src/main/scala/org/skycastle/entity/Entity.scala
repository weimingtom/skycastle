package org.skycastle.entity


import _root_.org.skycastle.entity.entitycontainer.EntityContainer
import accesscontrol._
import java.util.logging.{Logger, Level}
import script.Script
import util.{LogMethods, Parameters}
import util.ParameterChecker._
import network.Message
import org.skycastle.util.Properties
import java.lang.reflect.Method

/**
 * Represents some mutable object in the game (server or client).
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class Entity extends Properties with LogMethods {


  private var _id: EntityId = null

  /**
   * Initialize the ID when adding this Entity to an EntityContainer.
   * Should not be used from client code, so only visible to the entity package.
   */
  private[entity] def setId( newId : EntityId) {
    requireNotNull( newId, 'newId )
    requireSizeEquals( newId.path, 'newId_path , 1 )
    
    _id = newId
  }

  /**
   * Identifier of this Entity.
   * Initialized when the entity is added to an EntityContainer.
   */
  def id : EntityId = _id

  /**
   * Used for accessing operations involving the system that keeps track and stores all entities.
   * Initialized when the Entity is added to an EntityContainer.
   * (TODO: Can we use scala to indicate that it should only be changed by the entity container somehow?  E.g. private[entitycontainer]?)
   */
  var container : EntityContainer = null

  /**
   * Any scripted actions added to this Entity
   */
  private val dynamicActions: Map[Symbol, Script] = Map()

  /**
   * The roles for users based security access control to the actions of this Entity.
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
  private var currentAction : Symbol = null

  // TODO: Maybe add RoleMember that is a check if caller id is in some collection in a property -> use some collections of entity id:s in properties as users members?
  // Complex cases could be e.g. Organization maintenance, handling different guild functions, etc.

  addRole( 'roleEditor )

  def getRoles : List[Role] = roles

  def getRole( roleId : Symbol ) : Option[Role] = roles.find( _.roleId == roleId )
  def hasRole( roleId : Symbol ) : Boolean = roles.exists( _.roleId == roleId )

  /**
   * Called after the Entity has been created and added to an EntityContainer.
   */
  private[entity] final def initEntity( initializationParameters : Parameters ) {
    onInit( if (initializationParameters == null) Parameters.empty else initializationParameters ) 
  }

  /**
   * Called after the Entity has been created and added to an EntityContainer.
   */
  protected def onInit( initializationParameters : Parameters ) {}

  @users( "roleEditor"  )
  @parameters( "roleId"  )
  def addRole( roleId : Symbol ) {
    requireNotNull(roleId, 'roleId)

    // TODO: Check users id syntax?  No special chars, java style identifier?
    if (roleId != null) {
      getRole(roleId) match {
        case Some(role) => logWarning( "Can not add role '"+roleId+"', it already exists." )
        case None => roles = roles ::: List( new Role( roleId ) )
      }
    }
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
      case None => logWarning( "Can not add '"+member+"' to role '"+roleId+"', no such role found." )
    }
  }

  @users( "roleEditor"  )
  @parameters( "roleId, member" )
  def removeRoleMember( roleId : Symbol, member : RoleMember ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(member, 'member)

    getRole(roleId) match {
      case Some(role:Role) => role.removeMember( member )
      case None => logWarning( "Can not remove '"+member+"' from role '"+roleId+"', no such role found." )
    }
  }

  @users( "roleEditor"  )
  @parameters( "roleId, actionId"  )
  def addRoleActionCapability( roleId : Symbol, allowedAction : Symbol ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(allowedAction, 'allowedAction)

    addRoleCapability( roleId, ActionCapability( allowedAction ) )
  }

  @users( "roleEditor"  )
  @parameters( "roleId, actionId"  )
  def removeRoleActionCapability( roleId : Symbol, allowedAction : Symbol ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(allowedAction, 'allowedAction)

    removeRoleCapability( roleId, ActionCapability( allowedAction ) )
  }

  def addRoleCapability( roleId : Symbol, capability : Capability ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(capability, 'capability)

    getRole(roleId) match {
      case Some(role:Role) => role.addCapability( capability )
      case None => logWarning( "Can not add capability '"+capability+"' to role '"+roleId+"', no such role found." )
    }
  }

  def removeRoleCapability( roleId : Symbol, capability : Capability ) {
    requireNotNull(roleId, 'roleId)
    requireNotNull(capability, 'capability)

    getRole(roleId) match {
      case Some(role:Role) => role.removeCapability( capability )
      case None => logWarning( "Can not remove capability '"+capability+"' from role '"+roleId+"', no such role found." )
    }
  }

  // Special actions:
  // * Remove the entity
  // * Add / remove / change action


  private def callAllowed( caller: EntityId, actionId: Symbol ) : Boolean = {
    // Check access rights.  By default allow any call by this entity itself.
    // (the identity or privilegies of original caller are not retained when an action invokes another action,
    // instead the identity of the entity that contains the calling action is used.)
    caller == id ||
      roles.exists( _.allowsCall( caller, actionId ) )
  }

  def call( message : Message ) :Unit = call( message.callingEntity, message.calledAction, message.parameters )

  /**
   * Call an action available in this entity.
   */
  def call( caller : EntityId, actionId : Symbol, parameters : Parameters  ) :Unit = {

    currentCaller = caller
    currentAction = actionId

    try {
      ensureActionMethodsLoaded()

      if ( callAllowed(caller, actionId) ) {
        if (!callBuiltinAction(actionId, parameters))
          if(!callActionMethod(caller, actionId, parameters))
            if(!callDynamicAction(caller, actionId, parameters))
              logWarning( "Caller '"+caller+"' tried to call action '"+actionId+"' on entity "+id+", " +
                          "but no such action found.  Ignoring call." )
      }
      else {
        logWarning( "Caller '"+caller+"' is not authorized to call action '"+actionId+"' on entity "+id+" of type '"+getClass.getName+"', or no such method found.  Ignoring call." )
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
   * Call an action on an Entity managed by this Entity.
   * This is used for example for the connection Entities between the client and server.
   * Override if necessary, default implementation will just log an error.
   */
  def callContained( message : Message ) {
    logError( "The entity "+id+" doesn't support inner entities.  '"+message.callingEntity+"' tried to call action '"+message.calledAction+"' with parameters '"+message.parameters+"' on inner entity "+message.calledEntity+"." )
  }

  private def callDynamicAction( caller: EntityId, actionId: Symbol, parameters: Parameters ) : Boolean = {
    dynamicActions.get(actionId) match {
      case Some( action : Script ) =>
        action.run( this, parameters )
        true
      case None => false
    }
  }

  private def commaSeparatedStringToSymbolList( s : String) : List[Symbol] = {
    List.fromString( s, ',' ).flatMap( {entry : String =>
       val trimmedEntry = entry.trim()
       if ( trimmedEntry.length > 0) {
         List( Symbol( trimmedEntry ) )
       }
       else Nil
    })
  }

  @transient private var actionMethods : Map[ Symbol, ActionMethod ] = null
  private def findActionMethods() : Map[ Symbol, ActionMethod ] = {
    val thisClass = getClass()
    try {
      val methods : List[ Method ] = List.fromArray( thisClass.getMethods )
      val actionMethodsList = methods.filter{ (m : Method) => m.isAnnotationPresent( classOf[parameters] )}

      var actMethods : Map[Symbol,ActionMethod] = Map()

      actionMethodsList foreach { (m : Method) =>
        try {
          val actionAnnotation : parameters = m.getAnnotation( classOf[parameters] )
          val roleAnnotation : users = m.getAnnotation( classOf[users] )

          val parameterMapping = commaSeparatedStringToSymbolList( actionAnnotation.value )
          val roles = commaSeparatedStringToSymbolList( if( roleAnnotation==null) "" else roleAnnotation.value )

          if (parameterMapping.size == m.getParameterTypes.length) {

            val actionId = Symbol(m.getName)

            if (!roles.isEmpty) {
              val actionCallCapability = ActionCapability( actionId )
              roles foreach { roleId : Symbol => addRoleCapability( roleId, actionCallCapability ) }
            }

            val entry = (actionId, new ActionMethod( this, m, parameterMapping ))
            actMethods = actMethods + entry
          }
          else {
            logWarning( "the action method '"+m.getName+"' in Entity class "+thisClass.getName+" doesn't specify the names for all of its parameters " +
                    "(or specifies too many parameter names).  Found these parameter names: '"+parameterMapping.mkString(", ") +"', " +
                    "but required names for parameters of the following types: '"+m.getParameterTypes.mkString(", ") +"'. " )
          }
        }
        catch {
          case e : Exception => logWarning( "Problem when analysing action method '"+m.getName+"' in the Entty class "+thisClass.getName+": " + e.getMessage, e )
        }
      }

      actMethods
    }
    catch {
      case e : Exception => logWarning( "Problem when analysing methods in the Entty class "+thisClass.getName+": " + e.getMessage, e )
      Map()
    }
  }

  private def ensureActionMethodsLoaded() {

    if (!hasRole('everyone)) {
      addRole('everyone)
      addRoleMember( 'everyone, Everyone )
    }
    
    if (actionMethods == null) actionMethods = findActionMethods()

    // TODO: This will add duplicate capability entries for roles when the class is de-serialized, fix?
  }

  private def callActionMethod( caller: EntityId, actionId: Symbol, parameters: Parameters ) : Boolean = {
    actionMethods.get( actionId ) match {
      case Some( actionMethod : ActionMethod ) => {
        actionMethod.call( caller, parameters )
        true
      }
      case None => false
    }
  }


  /**
   * Allows for use of a simple switch clause to invoke any custom actions provided by decendant Entities.
   * Return true if the action was handled, false if not.
   */
  protected def callBuiltinAction(actionName: Symbol, parameters: Parameters): Boolean = { false }


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
        getRole( 'logListener ) match {
          case Some( role : Role ) => {
            if ( role.containsEntity( currentCaller ) ) {

              // Invoke a log feedback action on the other entity asynchronously
              callOtherEntity( currentCaller, 'callFeedback, Parameters(
                'callingEntity -> currentCaller,
                'calledEntity -> id,
                'calledEntityType -> getClass.getName,
                'calledAction -> currentAction,
                'logLevel -> level.getName,
                'logMessage -> message,
                'exception -> e.getMessage ) )
              
            }
          }
          case None =>
        }
      }

      // TODO: Some entities could maybe also be added as general log listeners for this entity,
      // e.g. a developer instead of the caller could listen to all logs for an entity

    }
  }


  def callOtherEntity( targetEntityId : EntityId, actionName : Symbol, parameters : Parameters ) {
    container.call( id, targetEntityId, actionName, parameters )
  }

  def callOtherEntity( message : Message ) {
    container.call( id, message.calledEntity, message.calledAction, message.parameters )
  }






  def getReferencedEntity( referenceproperty : Symbol ) : Option[ Entity ]  = {
    val entityId = getEntityId( referenceproperty, null )
    if (entityId == null || container == null) {
      None
    }
    else {
      container.getEntity( entityId )
    }
  }

  def getReferencedEntityForUpdate( referenceproperty : Symbol ) : Option[ Entity ]  = {
    val entityId = getEntityId( referenceproperty, null )
    if (entityId == null || container == null) {
      None
    }
    else {
      container.getEntityForUpdate( entityId )
    }
  }

  def requestParameter( listener : EntityActionId, property : Symbol ) {
    requestParameters( listener, ParametersExpression( Map() ) )
  }
  def requestParameters( listener : EntityActionId, parameterSources : ParametersExpression )  = {
    container.call( id, listener, parameterSources.getParameters( this ) )
  }


/* TODO: Implement

  def addListener( property : Symbol, listener : EntityActionId, parameterSources : ParametersExpression )  = { null }

  def addTrigger( property : Symbol,  trigger : Trigger, listener : EntityActionId, parameterSources : ParametersExpression)  = { null }
*/









}


