package org.skycastle.client


import entity.entitycontainer.{SimpleEntityContainer, EntityContainer}
import javax.swing.JLabel
import util.SimpleFrame

/**
 * Client side entrypoint 
 * 
 * @author Hans Haggstrom
 */

object SkycastleClient {

  val container : EntityContainer = new SimpleEntityContainer()

  def main( argv: Array[String]) {

    ClientLogger.logger.info( "Skycastle Client Started" )

    // Load player configured client side object from specified file if available
    // TODO

    // If not found, create default client side object
    // TODO

    // When connecting to a server, call an action that creates an Entity to represent the server and for communiction.

    // Create UI for the main client side entrypoint
    // TODO
    
    new SimpleFrame("Skycastgle Client", new JLabel( "Skycastle Client Test" ) )
  }


}