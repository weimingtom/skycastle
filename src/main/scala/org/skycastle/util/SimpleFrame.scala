package org.skycastle.util


import java.awt.Dimension
import javax.swing.{JComponent, JFrame}
/**
 * 
 * 
 * @author Hans Haggstrom
 */

class SimpleFrame(title : String, content : JComponent) extends JFrame {

  setTitle(title)
  setContentPane( content )
  setPreferredSize( new Dimension(800, 600) )

  setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )

  pack
  setVisible(true)
}

