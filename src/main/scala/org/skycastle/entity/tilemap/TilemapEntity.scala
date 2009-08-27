package org.skycastle.entity.tilemap


import util.{MathUtils, Parameters}

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
class TilemapEntity extends Entity {

  // Start with brute force...
  val MAX_NR_OF_TILETYPES = 255

  private var outsideTileType : EntityId = null
  private var entities : List[EntityId] = Nil
  private val tileTypes = new Array[EntityId]( MAX_NR_OF_TILETYPES )

  private var nextFreeTileType = 1

  private var sizeX = 1
  private var sizeY = 1
  private var sizeZ = 1

  private var mapData : Array[Byte] = null

  // Default map
  reset(3, 3, 1, null)

  private def numberOfTiles = sizeX * sizeY * sizeZ


  /**
   * Recreates the map as the specified size, clearing it at the same time.
   */
  def reset(sizeX_ : Int, sizeY_ : Int, sizeZ_ : Int, defaultTileType : EntityId )  {

    sizeX = MathUtils.clamp( sizeX_, 1, 100)
    sizeY = MathUtils.clamp( sizeY_, 1, 100)
    sizeZ = MathUtils.clamp( sizeZ_, 1, 10)

    mapData = new Array[Byte]( numberOfTiles )
    outsideTileType = defaultTileType
    tileTypes(0) = outsideTileType
    nextFreeTileType = 1
    entities = Nil
  }


  def createUi(parameters: Parameters) = {
    new Tilemap2dUi(this)
  }

  def addEntity( entityId : EntityId ) {
    entities = entityId :: entities
    // TODO: Notify listeners
  }

  def removeEntity( entityId : EntityId ) {
    entities = entities.remove( _ == entityId )
    // TODO: Notify listeners
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
  private def getTileCode( tileType : EntityId )  : Byte = {

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
  def getTileAt( x : Int, y : Int, z : Int ) : EntityId = {
    tileTypes( mapData( index(x, y, z) ) )
  }

  /**
   * Sets the TileArchetype id of the tile at the specified location
   */
  def setTileAt( x : Int, y : Int, z : Int, tileType : EntityId ) {
    mapData( index(x, y, z) ) = getTileCode( tileType )
    // TODO: Notify listeners
  }

  /**
   * Returns the entities in the specified tile 
   */
  def getEntitiesAt( x : Int, y : Int, z : Int ) : List[ EntityId ] = Nil // TODO

  /**
   * Returns the entities on the map
   */
  def getEntities() = entities

}



