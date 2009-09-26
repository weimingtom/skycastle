package org.skycastle.entity.entitycontainer


import collection.mutable.HashMap
import network.Message
import util.Parameters
import util.ParameterChecker._


/**
 * A straightforward single-threaded, non-persistent EntityContainer.
 * 
 * @author Hans Haggstrom
 */
class SimpleEntityContainer extends EntityContainer {

  private val entities = new HashMap[EntityId, Entity]()
  private val namedEntities = new HashMap[String, EntityId]()
  private var nextFreeId : Long = 1L
  private var queuesActionCalls : List[Message] = Nil

  private def nextId() : Long = {
    val id = nextFreeId
    nextFreeId += 1
    id
  }

  def storeEntity(entity: Entity, initializationParameters : Parameters) : EntityId = {

    if (entity.id != null) throw new IllegalStateException( "Can not store entity, it already has an id.  Entity = " + entity )
    
    val id = EntityId( "entity_" + nextId() )

    entity.setId( id )
    entity.container = this
    entities.put( id, entity )

    entity.initEntity( initializationParameters )

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
    requireNotNull( entity, 'entity )
    requireNotNull( name, 'name )
    if (entity.id == null) throw new IllegalArgumentException("Entity should already be added to an EntityContainer.")

    namedEntities.put( name, entity.id )
  }

  def getNamedEntity(name: String) : Option[Entity] = getEntity( namedEntities.getOrElse( name, null ) )

  def getNamedEntityForUpdate(name: String) : Option[Entity] = getEntityForUpdate( namedEntities.getOrElse( name, null ) )

  def removeBinding(name: String) = {
    if (name != null) namedEntities.removeKey( name )
  }


  def call(message : Message)  {
    queuesActionCalls = queuesActionCalls ::: List( message )
  }



  /**
   * Should be called regularily to execute queued action calls and tasks.
   */
  def update( currentTime_ms : Long ) {

    // Only process the actions that have collected before this update
    val actionsToDo = queuesActionCalls
    queuesActionCalls = Nil

    // Execute all calls
    actionsToDo foreach { call : Message =>

      val headEntityId = call.calledEntity.headEntityId
      getEntity( headEntityId ) match {
        case Some(entity : Entity) => {
          call.calledEntity.tailEntityId match  {
            case Some( innerEntityId ) => entity.callContained( call )
            case None => entity.call( call )
          }
        }
        case _ => EntityLogger.logError( "Can not process action call " +call+ ", entity "+headEntityId +" not found." )
      }
    }
  }

  /**
   * A main game loop that calls update at regular intervalls.
   */
  def start() {
    while (true ) {
      update( System.currentTimeMillis )

      Thread.sleep( 1 )
    }
  }

}

