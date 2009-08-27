package org.skycastle.client


import javax.swing.JLabel
import util.SimpleFrame

/**
 * Client side entrypoint 
 * 
 * @author Hans Haggstrom
 */

object SkycastleClient {

  def main( argv: Array[String]) {

    ClientLogger.logger.info( "Skycastle Client Started" )

    new SimpleFrame("Skycastgle Client", new JLabel( "Skycastle Client Test" ) )
  }


}