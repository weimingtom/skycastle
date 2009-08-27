package org.skycastle.server

import com.sun.sgs.app._
import entity.Entity
import entity.tilemap.{TilemapEntity}
import java.util.logging.{Level, Logger}
import java.util.Properties
import skycastle.util.Parameters


/**
 * Server side entrypoint for server initialization & connecting users
 *
 * @author Hans Haggstrom
 */
@SerialVersionUID(1)
@serializable
class SkycastleServer extends AppListener {
  var currentMap: ManagedReference[ManagedEntity[TilemapEntity]] = null


  /**
   * This is called to initialize the server
   */
  def initialize(properties: Properties) {

    ServerLogger.logger.info("Skycastle Server Started")

    val tilemap = new TilemapEntity()

    currentMap = AppContext.getDataManager.createReference(new ManagedEntity(tilemap))

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

