package org.skycastle.entity.tilemap


import util.{MathUtils, Properties}

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class TilemapEntity( archetype : ArchetypeId, sizeX : Int, sizeY : Int, sizeZ : Int )
        extends Entity( archetype : ArchetypeId ) {

  // Start with brute force...
  val MAX_NR_OF_TILETYPES = 255
  val numberOfTiles = sizeX * sizeY * sizeZ
  val mapData = new Array[Byte]( numberOfTiles )
  val outsideTileType : ArchetypeId = null
  var entities : List[EntityId] = List()
  val tileTypes = new Array[ArchetypeId]( MAX_NR_OF_TILETYPES )
  var nextFreeTileType = 0

  def invoke(actionName: String, parameters: Properties) = null

  def createUi(parameters: Properties) = null

  def addEntity( entityId : EntityId ) {
    entities = entityId :: entities
    // TODO: Update client side version
  }

  def removeEntity( entityId : EntityId ) {
    entities = entities.remove( _ == entityId )
    // TODO: Update client side version
  }

  private def index( x : Int, y : Int, z : Int ) : Int = {
    val x1 = MathUtils.clamp( x, 0, sizeX - 1 )
    val y1 = MathUtils.clamp( y, 0, sizeY - 1 )
    val z1 = MathUtils.clamp( z, 0, sizeZ - 1 )
    x1 + y1 * sizeX + z1 * sizeY * sizeX
  }

  /**
   * Return the code for the specified tile type, if not found it adds it, if no space for more tile types 0 is returned.
   */
  private def getTileCode( tileType : ArchetypeId  )  : Byte = {

    var tileIndex = tileTypes.findIndexOf( _ == tileType )
    if (tileIndex < 0) {
      if (nextFreeTileType < MAX_NR_OF_TILETYPES) {
        tileTypes( nextFreeTileType ) = tileType
        tileIndex = nextFreeTileType
        nextFreeTileType += 1
      }
      else tileIndex = 0 // Out of tile indexes
    }

    tileIndex.toByte
  }

  /**
   * Returns the TileArchetype id of the tile at the specified location
   */
  def getTileAt( x : Int, y : Int, z : Int ) : ArchetypeId = {
    tileTypes( mapData( index(x, y, z) ) )
  }

  /**
   * Sets the TileArchetype id of the tile at the specified location
   */
  def setTileAt( x : Int, y : Int, z : Int, tileType : ArchetypeId ) {
    mapData( index(x, y, z) ) = getTileCode( tileType )
  }

  /**
   * Returns the entities in the specified tile 
   */
  def getEntitiesAt( x : Int, y : Int, z : Int ) : List[ EntityId ] = List()

  /**
   * Returns the entities on the map
   */
  def getEntities() = entities

}


