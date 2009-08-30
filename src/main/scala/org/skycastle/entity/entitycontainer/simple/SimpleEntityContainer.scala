package org.skycastle.entity.entitycontainer


import collection.mutable.HashMap
/**
 * A straightforward single-threaded, non-persistent EntityContainer.
 * 
 * @author Hans Haggstrom
 */
class SimpleEntityContainer extends EntityContainer {

  private val entities = new HashMap[EntityId, Entity]()
  private val namedEntities = new HashMap[String, EntityId]()
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

}

