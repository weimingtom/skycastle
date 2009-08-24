package org.skycastle.entity.tilemap


import util.{MathUtils, Description, Properties}

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class TilemapArchetype(id : ArchetypeId, parameters : Properties) extends Archetype(id, parameters) {

  def update(updateType: String, parameters: Properties) = null

  def branch(newId: String, description: Description) = null

  override def createEntity(parameters: Properties) = {

    val sizeX = MathUtils.clamp( parameters.getInt( 'sizeX, 8 ), 1, 100)
    val sizeY = MathUtils.clamp( parameters.getInt( 'sizeY, 8 ), 1, 100)
    val sizeZ = MathUtils.clamp( parameters.getInt( 'sizeZ, 8 ), 1, 10)

    new TilemapEntity( id, sizeX, sizeY, sizeZ )

  }


}

