package org.skycastle.entity.entitycontainer


import collection.mutable.HashMap
/**
 * A straightforward single-threaded, non-persistent EntityContainer.
 * 
 * @author Hans Haggstrom
 */
class SimpleEntityContainer extends EntityContainer {

  private val entities = new HashMap[EntityId, Entity]()
  private var nextFreeId : Long = 1L

  private def nextId() : Long = {
    val id = nextFreeId
    nextFreeId += 1
    id
  }

  def storeEntity(entity: Entity) : EntityId = {

    if (entity.id != null) throw new IllegalStateException( "Can not store entity, it already has an id.  Entity = " + entity )
    
    val id = new EntityId( "entity-" + nextId() )

    entity.id = id
    entity.container = this
    entities.put( id, entity )
    id
  }

  def markForUpdate(entity: Entity) {}

  def getEntity(entityId: EntityId) : Option[Entity] = {
    entities.get( entityId )
  }

  def removeEntity(entityId: EntityId) {
    entities.removeKey( entityId )
  }

  def getEntityForUpdate(entityId: EntityId) = {
    getEntity(entityId)
  }
}

