package org.skycastle.entity


import org.skycastle.entity.entitycontainer.EntityContainer
import org.skycastle.entity.accesscontrol._
import java.util.logging.{Logger, Level}
import org.skycastle.entity.script.Script
import org.skycastle.util.ParameterChecker._
import org.skycastle.network.Message
import org.skycastle.util.{StringUtils, Parameters, LogMethods}
import org.skycastle.entity.properties._
import java.lang.reflect.{Member, AnnotatedElement, Field, Method}

/**
 * Represents some mutable object in the game (server or client).
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class Entity extends Properties with LogMethods with AccessControlMethods {


  private var _id: EntityId = null
  private var _container : EntityContainer = null

  /**
   * Any scripted actions added to this Entity
   */
  private var dynamicActions: Map[Symbol, Script] = Map()

  @transient private var actionMethods : Map[ Symbol, ActionMethod ] = null

  /**
   * The ID of the entity that is currently calling an action on this entity, or null if this entity is not processing
   * an action.
   *
   * Can be used e.g. to send back log messages to the calling entity if it is authorized and interested in receiving them.
   */
  @transient private var currentCaller : EntityId = null
  @transient private var currentAction : Symbol = null


  // Add roles that are used by the various default Entity methods.
  // OPTIMIZE: These add some size to an Entity, could be good if their explicit definition could be avoided.
  addRole('roleEditor )
  addRole('propertyReader )
  addRole('propertyEditor )
  addRole('propertyCreator )
  addRole('logListener )
  addRoleWithMembers('everyone,  Everyone )


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
  def container : EntityContainer = _container


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
   * Set the EntityContainer for this entity.
   * Should not be used from client code, so only visible to the entity package.
   */
  private[entity] def container_=( newContainer : EntityContainer) {
    _container = newContainer
  }



  /**
   * Called after the Entity has been created and added to an EntityContainer.
   */
  private[entity] final def initEntity( initializationParameters : Parameters ) {
    onInit( if (initializationParameters == null) Parameters.empty else initializationParameters ) 
  }

  /**
   * Called before the Entity is removed from its current EntityContainer.
   */
  private[entity] final def deinitEntity( ) {
    onRemoved()
  }


  /**
   * Called after the Entity has been created and added to an EntityContainer.
   */
  protected def onInit( initializationParameters : Parameters ) {}

  /**
   * Called before the Entity is removed from an EntityContainer.
   * Can e.g. be used to remove composite entities.
   */
  protected def onRemoved() {}


  

  /**
   * Sets the value of a property.
   *
   * Can have different permissions depending on the property?
   * But will default to allow editing access to the propertyEditor role.
   */
  @users( "propertyEditor"  )
  @parameters( "property, value"  )
  override def setProperty( property : Symbol, value : Any ) {
    super.setProperty( property, value )
  }

  /**
   * Returns the value of a property.
   *
   * Can have different permissions depending on the property?
   * But will default to allow reading access to the propertyEditor and propertyReader roles.
   */
  @users( "propertyEditor, propertyReader"  )
  @parameters( "property"  )
  @callback
  override def getProperty( property : Symbol ) : Option[Any] = {
    super.getProperty( property )
  }


  /**
   * Creates a new dynamical property.
   * By requiring properties to be created before use we reduce errors from misstyping a property value when
   * trying to set it, and accidentally creating a new property instead of updating an existing.
   *
   * Also allows specifying property metadata, type, invariants, etc.
   */
  @users( "propertyCreator"  )
  @parameters( "property, value, kind"  )
  def createProperty[T]( property : Symbol, value : T, kind : Class[T] )  {
    super.addProperty( property, value, kind )
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

      if (!callAllowed(caller, id, actionId, parameters)) {
        throw CallException("Call not authorized or action not found.  Ignoring call.")
      }
      else {
        if (!handleActionCall(actionId, parameters))
          if (!callActionMethod(caller, actionId, parameters))
            if (!callDynamicAction(caller, actionId, parameters))
              throw CallException("No such action found.  Ignoring call.")
      }
    } catch {
       case e : Throwable => {
         logError( "Exception when '"+caller+"' called action '"+actionId.name+"' with parameters '"+parameters+"' on entity with id "+id+" of type '"+getClass.getName+"' : " + e, e )
       }
    }
    finally {
      currentAction = null
      currentCaller = null
    }
  }

  /**
   * Call an action on an Entity managed by this Entity.
   * This is used for example for the connection Entities between the client and server.
   * Override if necessary, default implementation will just log an error.
   */
  def callContained( message : Message ) {
    throw CallException( "The entity eith id "+id+" doesn't support inner entities.  '"+message.callingEntity+"' tried to call action '"+message.calledAction+"' with parameters '"+message.parameters+"' on inner entity "+message.calledEntity+"." )
  }


  private def ensureActionMethodsLoaded() {
    // TODO: We could use a common structure for these for all instances of a class of a specific type -> some object to cache them?
    // TODO: This will add duplicate capability entries for roles when the class is de-serialized, fix?
    if (actionMethods == null) actionMethods = findActionMethods()
  }

/*
  private def ensurePropertyFieldsLoaded() {
    // TODO: We could use a common structure for these for all instances of a class of a specific type -> some object to cache them?
    // TODO: This will add duplicate capability entries for roles when the class is de-serialized, fix?
    if (propertyFields == null) propertyFields = findPropertyFields()
  }
*/

  private def findActionMethods() : Map[ Symbol, ActionMethod ] = {
    val thisClass = getClass()
    try {
      val methods : List[ Method ] = List.fromArray( thisClass.getMethods )
      val actionMethodsList = methods.filter{ (m : Method) => m.isAnnotationPresent( classOf[parameters] )}

      // Some user friendly checking
      val nonActionMethods = methods diff actionMethodsList
      warnAboutInvalidActions( nonActionMethods, classOf[users] )

      var actMethods : Map[Symbol,ActionMethod] = Map()

      actionMethodsList foreach { (m : Method) =>
        try {
          val parameterMapping = getAnnotatedSymbols( m.getAnnotation( classOf[parameters] ).value )
          val roles            = getAnnotatedSymbols( m.getAnnotation( classOf[users] ).value  )

          if (parameterMapping.size == m.getParameterTypes.length) {

            val actionId = Symbol(m.getName)

            addRoleCapabilities( roles, ActionCapability( actionId ) )

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

/*
  private def findPropertyFields() : Map[ Symbol, PropertyField ] = {
    val thisClass = getClass()
    try {
      val fields : List[ Field ] = List.fromArray( thisClass.getFields )
      val propertyFieldsList = fields.filter{ (f : Field) => classOf[Property[_]].isAssignableFrom( f.getClass ) }

      // Some user friendly checking
      val nonPropertyFields = fields diff propertyFieldsList

      // TODO: Fix, looks like Scala doesn't store vars as java fields in the normal place at least. 
      warnAboutInvalidProperties( nonPropertyFields, classOf[readers] )
      warnAboutInvalidProperties( nonPropertyFields, classOf[editors] )

      var propFields : Map[Symbol,PropertyField] = Map()

      propertyFieldsList foreach { (f : Field) =>
        try {
          val propertyId = Symbol(f.getName)

          val readers = getAnnotatedSymbols( f.getAnnotation( classOf[readers] ).value )
          val editors = getAnnotatedSymbols( f.getAnnotation( classOf[editors] ).value )

          addRoleCapabilities( readers, ReadCapability( propertyId ) )
          addRoleCapabilities( editors, EditCapability( propertyId ) )

          val entry = (propertyId, new PropertyField( this, f ))
          propFields = propFields + entry
        }
        catch {
          case e : Exception => logWarning( "Problem when analysing action field '"+f.getName+"' in the Entty class "+thisClass.getName+": " + e.getMessage, e )
        }
      }

      propFields
    }
    catch {
      case e : Exception => logWarning( "Problem when analysing methods in the Entty class "+thisClass.getName+": " + e.getMessage, e )
      Map()
    }
  }
*/

/*
  private def warnAboutInvalidProperties( members : List[Field], annotation : Class[_ <: java.lang.annotation.Annotation] ) {
    members filter{ _.isAnnotationPresent( annotation ) } foreach { (f : Field ) =>
      logWarning( "The field '"+f.getName+"' in the Entity '"+this.getClass.getName+"' has the annotation '"+annotation+"', " +
                  "but it is not of type "+classOf[Property[_]]+", so it will not be treated as a property." )
    }
  }
*/

  private def warnAboutInvalidActions( members : List[Method], annotation : Class[_ <: java.lang.annotation.Annotation] ) {
    members filter{ _.isAnnotationPresent( annotation ) } foreach { (m : Method) =>
      logWarning( "The method '"+m.getName+"' in the Entity '"+this.getClass.getName+"' has the annotation '"+annotation+"', " +
                  "but it doesn't have the annotation "+classOf[parameters]+", so it will not be treated as an Action." )
    }
  }


  private def addRoleCapabilities( roleIds : List[Symbol], capability : => Capability ) {
    // Lazily create only one instance of the Capability
    if (!roleIds.isEmpty) {
      val c = capability
      roleIds foreach { roleId : Symbol => addRoleCapability( roleId, c ) }
    }

  }

  private def getAnnotatedSymbols( v : String ) : List[Symbol] = {

    val value : String = if (v == null) "" else v
    
    StringUtils.commaSeparatedStringToSymbolList( value )
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
   * Allows for use of a simple switch clause to invoke any custom actions provided by descendant Entities.
   * Return true if the action was handled, false if not.
   */
  protected def handleActionCall(actionName: Symbol, parameters: Parameters): Boolean = { false }


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






/*
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
*/


/*
  def requestParameter( listener : EntityActionId, property : Symbol ) {
    requestParameters( listener, ParametersExpression( Map() ) )
  }

  def requestParameters( listener : EntityActionId, parameterSources : ParametersExpression )  = {
    container.call( id, listener, parameterSources.getParameters( this ) )
  }
*/


/* TODO: Implement

  def addListener( property : Symbol, listener : EntityActionId, parameterSources : ParametersExpression )  = { null }

  def addTrigger( property : Symbol,  trigger : Trigger, listener : EntityActionId, parameterSources : ParametersExpression)  = { null }
*/



  protected def callDynamicAction( caller: EntityId, actionId: Symbol, parameters: Parameters ) : Boolean = {
    dynamicActions.get(actionId) match {
      case Some( action : Script ) =>
        action.run( this, parameters )
        true
      case None => false
    }
  }

// TODO: Add / remove / change action


}


