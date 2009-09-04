package org.skycastle.ui.components


import content.composite.CompositeEntity
import javax.swing.JPanel
import net.miginfocom.swing.MigLayout
import util.Parameters

/**
 *
 *
 * @author Hans Haggstrom
 */

@serializable
@SerialVersionUID(1)
class PanelUi extends Ui {

  type ViewType = JPanel
  
  override val childrenSupported = true

  lazy val layout = new MigLayout()

  def createOwnView(composite: CompositeEntity) = new JPanel(layout)

  protected def updateViewProperties(view: ViewType, changedParameters: Parameters)  {
    if (changedParameters.contains('layout))
      layout.setLayoutConstraints(changedParameters.getString('layout, ""))

    if (changedParameters.contains('columnLayout))
      layout.setLayoutConstraints(changedParameters.getString('columnLayout, ""))

    if (changedParameters.contains('rowLayout))
      layout.setLayoutConstraints(changedParameters.getString('rowLayout, ""))
  }
}

