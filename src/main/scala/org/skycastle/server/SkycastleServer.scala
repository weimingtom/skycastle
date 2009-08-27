package org.skycastle.server

import _root_.org.skycastle.entity.EntityId
import com.sun.sgs.app._
import entity.entitycontainer.darkstar.DarkstarEntityContainer
import entity.tilemap.{TilemapEntity}
import java.util.Properties


/**
 * Server side entrypoint for server initialization & connecting users
 *
 * @author Hans Haggstrom
 */
@SerialVersionUID(1)
@serializable
class SkycastleServer extends AppListener {
  //var currentMap: ManagedReference[ManagedEntity[TilemapEntity]] = null

  var currentMapId : EntityId = null


  /**
   * This is called to initialize the server
   */
  def initialize(properties: Properties) {

    ServerLogger.logger.info("Skycastle Server Started")

    // Create testworld
    val tilemap = new TilemapEntity()

    // Store testworld and retain its id
    currentMapId = DarkstarEntityContainer.storeEntity( tilemap )

    //currentMap = AppContext.getDataManager.createReference(new ManagedEntity(tilemap))

  }

  /**
   * This is called when a user connects
   */
  def loggedIn(session: ClientSession) : ClientSessionListener  = {

    // Get or create user
    val user = User.logUserIn( session )

    // Get or create entity that presents the server options for the user
    // (if the user has admin status the screen can be different / provide additional options)
    

    // Send user the server main screen


    user
  }


}

