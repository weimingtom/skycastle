package org.skycastle.entity.entitycontainer


import collection.mutable.HashMap
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class ClientEntityContainer extends EntityContainer {

  val entities = new HashMap[EntityId, Entity]()

  def callEntityAction(calledId: CallerId, entityId: EntityId, actionCall: ActionCall) = null

  def removeEntity(calledId: CallerId, entityId: EntityId) = null

  def createEntity(calledId: CallerId, entityId: EntityId, entityType: String, parameters: Parameters, initializationScript: Script) {


  }
}

