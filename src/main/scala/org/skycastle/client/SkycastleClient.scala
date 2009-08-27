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

  var container : EntityContainer = null

  def main( argv: Array[String]) {

    ClientLogger.logger.info( "Skycastle Client Started" )


    container = new SimpleEntityContainer()


    new SimpleFrame("Skycastgle Client", new JLabel( "Skycastle Client Test" ) )
  }


}