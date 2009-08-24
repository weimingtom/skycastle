package org.skycastle.server

import com.sun.sgs.app.{ClientSession, AppListener}
import java.util.logging.Logger
import java.util.Properties

/**
 * Logging holder
 */
object SkycastleServerLogger {

  val logger = Logger.getLogger( "org.skycastle.server.SkycastleServer" )
}



/**
 * Server side entrypoint for server initialization & connecting users
 * 
 * @author Hans Haggstrom
 */
@SerialVersionUID( 1 )
@serializable
class SkycastleServer extends AppListener  {


  /**
   * This is called to initialize the server
   */
  def initialize(props: Properties) = {

    SkycastleServerLogger.logger.info( "Skycastle Server Started" )

  }

  /**
   * This is called when a user connects
   */
  def loggedIn(session: ClientSession) = null
}