package org.skycastle.entity.entitycontainer


import script.ActionCall
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
  // TODO: Do not allow outside calls.  Instead allow clients and servers to create proxy entities within each other, that behave like normal entities in the container.
  // The client creates an entity for the server inside itself when connecting to the server, and can thus control what the server is allowed to do in the client side EntityContainer.
  // The server likewise creates an entity for the user when it connects to the game, and can control what access rights the user gets to which entities.
  // Different games could also create proxy entities for players, that allow the players to interact and receive perceptions from the gameworld.
  // Each game would then have its own EntityContainer that it runs in, but could also have a proxy entity in the hosting gameworld that can observe it (e.g. for games like in-game football played with normal game characters, a football proxy game could observe the playfield and count scores)
  @deprecated
  def callEntityAction( callerId : EntityId, entityId : EntityId, actionCall : ActionCall )

  // Make removal of an entity a special action on it, allowing easier administration of it.
  /* *
   * Removed the specified entity from the container.
   */
  //def removeEntity( calledId : CallerId, entityId : EntityId )

}

