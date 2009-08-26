package org.skycastle.client


import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.{JLabel, JFrame}
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