package org.skycastle.entity.tilemap


import util.{Parameters, Description}


/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
class TileArchetype( parameters : Parameters )  extends Archetype( parameters )  {

  val textureName = parameters.getString( 'textureName, "blank" )

  def update(updateType: String, parameters: Parameters) = null

  def branch() = null
}

