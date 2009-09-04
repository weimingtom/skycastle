package org.skycastle.ui.components


import content.composite.CompositeEntity
import javax.swing.{JPanel, JLabel}
import util.Parameters

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

    if (changedParameters.contains('text)) {
      view.setText( parameters.getString( 'text, "") )
    }

  }

}

