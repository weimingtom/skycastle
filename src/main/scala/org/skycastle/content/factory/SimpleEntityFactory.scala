package org.skycastle.content.factory


import entity.Entity
import entity.script.Script
import util.{ClassUtils, Parameters}
import util.ParameterChecker._

/**
 * 
 */
class SimpleEntityFactory( val info : Parameters, entityClassName : String, expectedType : Class[_ <: Entity], creationParameters : Parameters, initializationScript : Script ) extends EntityFactory {

  requireNotNull( info, 'info )
  requireNotNull( entityClassName, 'entityClassName )

//  def info = information

  def createEntity(parameters: Parameters) : Entity = {

    val entity : Entity = container.createEntity( entityClassName, expectedType, creationParameters )

    if (initializationScript != null) initializationScript.run( entity, Parameters() )

    entity
  }

}