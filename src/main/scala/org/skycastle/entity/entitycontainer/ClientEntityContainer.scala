package org.skycastle.entity.entitycontainer


import collection.mutable.HashMap
import script.{ActionCall, Script}
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class ClientEntityContainer extends EntityContainer {

  val entities = new HashMap[EntityId, Entity]()


  def callEntityAction(callerId: EntityId, entityId: EntityId, actionCall: ActionCall) = null // TODO
}

