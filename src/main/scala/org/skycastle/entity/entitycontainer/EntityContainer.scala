package org.skycastle.entity.entitycontainer


import util.Parameters

/**
 * Something that can contain and manage entities.
 *
 * Typically a server and client each have one of these.
 *
 * This interface is used by some user of the entity container.  The methods may or may not be allowed depending on the access rights of the user
 * to the different entities and their actions.
 * 
 * @author Hans Haggstrom
 */
trait EntityContainer {

  // Do entity creation also by calling an entity factory action on a built-in entity with a fixed name.
  // That way object creation is also possible to administer easily
  /* *
   * Creates a new entity in the container, of the specified type and with the specified initial parameters.
   * Runs the initialization script on the newly created entity to configure it.
   */
//  def createEntity( calledId : CallerId, entityId : EntityId, entityType: String, parameters : Parameters, initializationScript : Script )

  /**
   * Used to invoke an action of an entity, with some specified parameters.
   */
  def callEntityAction( calledId : CallerId, entityId : EntityId, actionCall : ActionCall )

  // Make removal of an entity a special action on it, allowing easier administration of it.
  /* *
   * Removed the specified entity from the container.
   */
  //def removeEntity( calledId : CallerId, entityId : EntityId )

}

