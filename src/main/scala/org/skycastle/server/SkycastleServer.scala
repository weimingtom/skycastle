package org.skycastle.server

import com.sun.sgs.app.{ManagedReference, AppContext, ClientSession, AppListener}
import entity.Entity
import entity.tilemap.{TilemapEntity, TilemapArchetype}
import java.util.logging.Logger
import java.util.Properties
import util.Parameters


/**
 * Server side entrypoint for server initialization & connecting users
 * 
 * @author Hans Haggstrom
 */
@SerialVersionUID( 1 )
@serializable
class SkycastleServer extends AppListener  {

  var currentMap : ManagedReference[ManagedEntity] = null


  /**
   * This is called to initialize the server
   */
  def initialize(props: Properties)  {

    ServerLogger.logger.info( "Skycastle Server Started" )

    val archetype: TilemapArchetype = new TilemapArchetype(new Parameters())
    new ManagedArchetype( archetype ) // Stores a managed reference to the archetype and initializes its id.

    val tilemap: TilemapEntity = archetype.createEntity(new Parameters())

    currentMap = AppContext.getDataManager.createReference( new ManagedEntity( tilemap ) )

  }

  /**
   * This is called when a user connects
   */
  def loggedIn(session: ClientSession) {

    // Create player entity, add to map, set to listen to map changes?
    // Send map etc to player
    
  }

}