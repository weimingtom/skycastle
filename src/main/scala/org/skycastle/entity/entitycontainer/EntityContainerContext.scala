package org.skycastle.entity.entitycontainer

/**
 * Interface to the EntityContainer to entities inside it.
 * 
 * @author Hans Haggstrom
 */
trait EntityContainerContext {

  /**
   * Returns the entity with the specified id, or None if no such entity was found.
   * Use this if the entity is likely not going to be modified.
   */
  def getEntity( entityId : EntityId ) : Option[Entity]

  /**
   * Returns the entity with the specified id, or None if no such entity was found.
   * Use this if the entity is going to be modified.
   */
  def getEntityForUpdate( entityId : EntityId ) : Option[Entity]

  /**
   * Indicates that the entity will be modified.  Use this to mark an entity fetched with getEntity for modification.
   */
  def markForUpdate( entity : Entity )

  /**
   * Stores the entity to the EntityContainer if it isn't already, and returns its id.
   */
  def storeEntity( entity : Entity ) : EntityId

  /**
   * Removes the entity from the EntityContainer.
   */
  def removeEntity( entityId : EntityId )


  // TODO: Add interfaces for scheduled action callbacks etc.

}

