package org.skycastle.content.factory


import annotations.ActionMethod
import entity.Entity
import util.Parameters

/**
 * An entity that is used for creating other entities of a particular type.
 * 
 * @author Hans Haggstrom
 */
class FactoryEntity extends Entity {

  def createEntity() : Entity = {
    null
  }

  // TODO

}

