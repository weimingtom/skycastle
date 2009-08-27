package org.skycastle.entity


import annotations.ActionMethod
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

