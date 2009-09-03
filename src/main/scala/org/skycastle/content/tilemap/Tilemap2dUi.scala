package org.skycastle.entity.tilemap


import javax.swing.JComponent
import ui.Ui
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class Tilemap2dUi(tilemap : TilemapEntity) extends Ui {
  type ViewType = JComponent

  def createOwnView() = null

  protected def updateViewProperties(view: ViewType, changedParameters: Parameters) = null

}

