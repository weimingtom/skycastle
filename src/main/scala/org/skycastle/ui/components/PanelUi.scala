package org.skycastle.ui.components


import org.skycastle.content.composite.CompositeEntity
import javax.swing.JPanel
import net.miginfocom.swing.MigLayout
import org.skycastle.util.Parameters
import org.skycastle.ui.Ui

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
    if (changedParameters.hasProperty('layout))
      layout.setLayoutConstraints(changedParameters.getString('layout, ""))

    if (changedParameters.hasProperty('columnLayout))
      layout.setLayoutConstraints(changedParameters.getString('columnLayout, ""))

    if (changedParameters.hasProperty('rowLayout))
      layout.setLayoutConstraints(changedParameters.getString('rowLayout, ""))
  }
}

