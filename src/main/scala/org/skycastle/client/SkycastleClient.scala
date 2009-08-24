package org.skycastle.client


import java.awt.Dimension
import java.util.logging.Logger
import javax.swing.{JLabel, JFrame}
/**
 * Client side entrypoint 
 * 
 * @author Hans Haggstrom
 */

object SkycastleClient {

  def main( argv: Array[String]) {

    ClientLogger.logger.info( "Skycastle Client Started" )

    val frame = new JFrame()
    frame.setTitle("Skycastle Client")
    frame.setContentPane( new JLabel( "Skycastle Client Test" ) )
    frame.setPreferredSize( new Dimension(800, 600) )
    frame.pack
    frame.setVisible(true)

  }


}