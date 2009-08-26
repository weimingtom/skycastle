package org.skycastle.ui


import entity.Archetype
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


  override def createEntity(parameters: Parameters) : ScreenEntity = {
    new ScreenEntity( id )
  }
}

