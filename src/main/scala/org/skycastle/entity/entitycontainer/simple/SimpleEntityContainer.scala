package org.skycastle.entity.entitycontainer


import collection.mutable.HashMap
import util.Parameters
/**
 * A straightforward single-threaded, non-persistent EntityContainer.
 * 
 * @author Hans Haggstrom
 */
class SimpleEntityContainer extends EntityContainer {

  private val entities = new HashMap[EntityId, Entity]()
  private val namedEntities = new HashMap[String, EntityId]()
  private var nextFreeId : Long = 1L
  private var queuesActionCalls : List[ActionCall] = Nil

  private def nextId() : Long = {
    val id = nextFreeId
    nextFreeId += 1
    id
  }

  def storeEntity(entity: Entity) : EntityId = {

    if (entity.id != null) throw new IllegalStateException( "Can not store entity, it already has an id.  Entity = " + entity )
    
    val id = EntityId( "entity_" + nextId() )

    entity.setId( id )
    entity.container = this
    entities.put( id, entity )

    entity.initEntity()

    id
  }

  def markForUpdate(entity: Entity) {}

  def getEntity(entityId: EntityId) : Option[Entity] = {
    if (entityId == null) None
    else entities.get( entityId )
  }

  def removeEntity(entityId: EntityId) {
    if (entityId != null) entities.removeKey( entityId )
  }

  def getEntityForUpdate(entityId: EntityId) = {
    getEntity(entityId)
  }


  def bindName(name: String, entity: Entity) {
    // TODO: Log warning & ignore on null entity or null name?
    if (entity == null) throw new IllegalArgumentException("Entity should not be null")
    if (name== null) throw new IllegalArgumentException("Name should not be null")

    val id = if (entity.id != null) entity.id
             else storeEntity( entity )

    namedEntities.put( name, id )
  }

  def getNamedEntity(name: String) : Option[Entity] = getEntity( namedEntities.getOrElse( name, null ) )

  def getNamedEntityForUpdate(name: String) : Option[Entity] = getEntityForUpdate( namedEntities.getOrElse( name, null ) )

  def removeBinding(name: String) = {
    if (name != null) namedEntities.removeKey( name )
  }


  def call(callingEntity: EntityId, calledEntity: EntityId, actionName: Symbol, parameters: Parameters)  {
    queuesActionCalls = queuesActionCalls ::: List(ActionCall( callingEntity, calledEntity, actionName, parameters ))
  }



  /**
   * Should be called regularily to execute queued action calls and tasks.
   */
  def update( currentTime_ms : Long ) {

    // Only process the actions that have collected before this update
    val actionsToDo = queuesActionCalls
    queuesActionCalls = Nil

    // Execute all calls
    actionsToDo foreach { call : ActionCall =>
      getEntityForUpdate( call.calledEntity ) match {
        case Some(entity : Entity) => entity.call( call.callingEntity, call.actionName, call.parameters )
        case _ => EntityLogger.logDebug( "Can not process action call " +call+ ", entity "+call.calledEntity+" not found." )
      }
    }
  }

  /**
   * A main game loop that calls update at regular intervalls.
   */
  def start() {
    while (true ) {
      update( System.currentTimeMillis )

      Thread.sleep( 10 )
    }
  }

}

