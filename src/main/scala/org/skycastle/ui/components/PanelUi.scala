package org.skycastle.ui.components


import javax.swing.JPanel
import net.miginfocom.swing.MigLayout
import util.Parameters

/**
 *
 *
 * @author Hans Haggstrom
 */

class PanelUi extends Ui(true) {

  type ViewType = JPanel

  lazy val layout = new MigLayout()

  def createOwnView() = new JPanel(layout)

  def onUpdate(view: ViewType, changedParameters: Parameters) {
    if (changedParameters.contains('layout))
      layout.setLayoutConstraints(changedParameters.getString('layout, ""))

    if (changedParameters.contains('columnLayout))
      layout.setLayoutConstraints(changedParameters.getString('columnLayout, ""))

    if (changedParameters.contains('rowLayout))
      layout.setLayoutConstraints(changedParameters.getString('rowLayout, ""))
  }
}

