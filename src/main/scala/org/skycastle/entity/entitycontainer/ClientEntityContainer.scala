package org.skycastle.entity.entitycontainer


import collection.mutable.HashMap
/**
 * 
 * 
 * @author Hans Haggstrom
 */
class ClientEntityContainer extends EntityContainer {

  val entities = new HashMap[EntityId, Entity]()

  def addEntity( entity : Entity ) {
    
  }



}

