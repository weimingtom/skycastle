package org.skycastle.ui


import javax.swing.JComponent

/**
 * 
 * 
 * @author Hans Haggstrom
 */
abstract class Ui {

  /**
   * Swing component for this UI.
   */
  lazy val view : JComponent = createView()

  protected def createView() : JComponent
  
}

