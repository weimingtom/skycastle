package org.skycastle.ui.components


import javax.swing.{JPanel, JLabel}
import util.Parameters
import org.skycastle.content.composite.CompositeEntity
import org.skycastle.util.Parameters
import org.skycastle.ui.Ui

/**
 * 
 * 
 * @author Hans Haggstrom
 */

@serializable
@SerialVersionUID(1)
class LabelUi extends Ui {

  type ViewType = JLabel

  def createOwnView(composite: CompositeEntity) = new JLabel()

  protected def updateViewProperties(view: ViewType, changedParameters: Parameters)  {

    if (changedParameters.hasProperty('text)) {
      view.setText( parameters.getString( 'text, "") )
    }

  }

}

