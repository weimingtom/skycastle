package org.skycastle.entity


import annotations.ActionMethod

/**
 * An entity that is used for creating other entities of a particular type.
 * 
 * @author Hans Haggstrom
 */
class FactoryEntity extends Entity {

  @ActionMethod
  def createEntity()

}

