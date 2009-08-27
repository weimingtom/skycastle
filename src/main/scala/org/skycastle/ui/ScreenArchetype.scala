package org.skycastle.ui


import entity.{EntityInitializationScript, Archetype}
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ScreenArchetype(parameters : Parameters ) extends Archetype(parameters ) {
  def update(updateType: String, parameters: Parameters) = null

  def branch() = null

  override def createEntity(creationParameters: Parameters) : ScreenEntity = {
    val screen = new ScreenEntity( id )
    val script = parameters.getAs[EntityInitializationScript]( 'entityInitializationScript, null )
    if (script != null) {
      script.run( screen )
    }

    screen
  }
}

