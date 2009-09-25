package org.skycastle.content.factory


import entity.{parameters, EntityId, Entity}
import util.Parameters

/**
 * An Entity that can be used to create other entities.
 */
@serializable
@SerialVersionUID(1)
trait EntityFactory extends Entity {

  /**
   * Returns some info of an entity that would be created by this factory with the specified parameters.
   *
   * The returned parameters will contain at least a description field, possibly icon and other fields too.
   */
  def getDescription( parameters : Parameters ) : Parameters

  /**
   * Creates and stores a new entity, using the specified parameters.
   * 
   * Returns the new entity.
   */
  def createEntity( parameters : Parameters ) : Entity

}