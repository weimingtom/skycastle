package org.skycastle.entity.tilemap


import javax.swing.{JComponent, JPanel}
import ui.Ui
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class Tilemap2dUi(tilemap : TilemapEntity) extends Ui(false) {
  type ViewType = JComponent

  def createOwnView() = null

  def onUpdate(view: ViewType, changedParameters: Parameters) = null
}

