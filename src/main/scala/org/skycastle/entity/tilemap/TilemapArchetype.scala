package org.skycastle.entity.tilemap


import util.{MathUtils, Description, Parameters}

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
class TilemapArchetype(parameters : Parameters) extends Archetype(parameters) {

  def update(updateType: String, parameters: Parameters) = null

  def branch() = null

  override def createEntity(parameters: Parameters) : TilemapEntity = {

    val sizeX = MathUtils.clamp( parameters.getInt( 'sizeX, 8 ), 1, 100)
    val sizeY = MathUtils.clamp( parameters.getInt( 'sizeY, 8 ), 1, 100)
    val sizeZ = MathUtils.clamp( parameters.getInt( 'sizeZ, 8 ), 1, 10)

    new TilemapEntity( id, sizeX, sizeY, sizeZ )

  }


}

