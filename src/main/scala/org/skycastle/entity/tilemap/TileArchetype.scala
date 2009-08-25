package org.skycastle.entity.tilemap


import util.{Properties, Description}


/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
class TileArchetype( id : ArchetypeId, parameters : Properties )  extends Archetype( id , parameters )  {

  val textureName = parameters.getString( 'textureName, "blank" )

  def update(updateType: String, parameters: Properties) = null

  def branch(newId: String, description: Description) = null
  
}

