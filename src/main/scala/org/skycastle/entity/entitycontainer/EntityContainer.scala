package org.skycastle.entity.entitycontainer


import network.Message
import util.{ClassUtils, Parameters}
import util.ParameterChecker._

/**
 * Something that can contain and manage entities.
 *
 * Typically a server and client each have one of these.
 *
 * This interface is used by entities contained inside the container,
 * as well as for programmatic direct access to the container.
 * 
 * @author Hans Haggstrom
 */
trait EntityContainer {

  /**
   * Creates and returns an entity of the specified type.
   * The entity is stored to the container also.
   *
   * Throws an ObjectCreationException if the specified class was not found, or was not of the specified kind.
   */
  def createEntity[T <: Entity]( kind : Class[T], initializationParameters : Parameters ) : T = {
    requireNotNull( kind, 'kind )
    createEntity( kind.getName, kind, initializationParameters )
  }


  /**
   * Creates and returns an entity of the specified (super) type, using the class with the specified name.
   * The entity is stored to the container also.
   *
   * Throws an ObjectCreationException if the specified class was not found, or was not of the specified kind.
   */
  def createEntity[T <: Entity]( classname : String, kind : Class[T], initializationParameters : Parameters ) : T = {
    requireNotNull( classname, 'classname )
    requireNotNull( kind, 'kind )

    val newEntity = ClassUtils.createObject( classname, kind )

    storeEntity( newEntity, initializationParameters )

    newEntity
  }


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
   * Indicates that the specified entity will be modified.
   */
  def markForUpdate( entity : Entity )

  /**
   * Stores the entity to the EntityContainer, and returns its id.
   * The entity should not already be stored.
   *
   * Calls the onInit method of the entity with the initializationParameters once the entity has been stored.
   */
  def storeEntity( entity : Entity, initializationParameters : Parameters ) : EntityId

  /**
   * Removes the entity from the EntityContainer.
   */
  def removeEntity( entityId : EntityId )


  def bindName( name : String, entity : Entity )
  def getNamedEntity( name : String ) : Option[Entity]
  def getNamedEntityForUpdate( name : String ) : Option[Entity]
  def removeBinding( name : String )

  /**
   * Asynchronously invokes the specified action on the specified entity, assuming the calling entity has the access rights for it.
   */
  def call( message : Message )

  /**
   * Asynchronously invokes the specified action on the specified entity, assuming the calling entity has the access rights for it.
   */
  def call( callingEntity : EntityId, calledEntity : EntityId, actionName : Symbol, parameters : Parameters ) {
    call( Message( callingEntity, calledEntity, actionName, parameters ) )
  }

  /**
   * Asynchronously invokes the specified action on the specified entity, assuming the calling entity has the access rights for it.
   */
  def call( callingEntity : EntityId, calledAction : EntityActionId, parameters : Parameters ) {
    call( callingEntity, calledAction.entity, calledAction.action, parameters )
  }


  // TODO: Add interfaces for scheduled action callbacks etc.






  // Do entity creation also by calling an entity factory action on a built-in entity with a fixed name.
  // That way object creation is also possible to administer easily
  /* *
   * Creates a new entity in the container, of the specified type and with the specified initial parameters.
   * Runs the initialization script on the newly created entity to configure it.
   */
//  def createEntity( calledId : CallerId, entityId : EntityId, entityType: String, parameters : Parameters, initializationScript : Script )

  /**
   *  Used to invoke an action of an entity, with some specified parameters.
   */
  // TODO: Do not allow outside calls.  Instead allow clients and servers to create proxy entities within each other, that behave like normal entities in the container.
  // The client creates an entity for the server inside itself when connecting to the server, and can thus control what the server is allowed to do in the client side EntityContainer.
  // The server likewise creates an entity for the user when it connects to the game, and can control what access rights the user gets to which entities.
  // Different games could also create proxy entities for players, that allow the players to interact and receive perceptions from the gameworld.
  // Each game would then have its own EntityContainer that it runs in, but could also have a proxy entity in the hosting gameworld that can observe it (e.g. for games like in-game football played with normal game characters, a football proxy game could observe the playfield and count scores)
//  @deprecated
//  def callEntityAction( callerId : EntityId, entityId : EntityId, actionCall : ActionCall )

  // Make removal of an entity a special action on it, allowing easier administration of it.
  /* *
   * Removed the specified entity from the container.
   */
  //def removeEntity( calledId : CallerId, entityId : EntityId )

}

