package org.skycastle.ui.components


import javax.swing.{JPanel, JLabel}
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class LabelUi extends Ui {

  type ViewType = JLabel

  def createOwnView() = new JLabel()

  protected def updateViewProperties(view: ViewType, changedParameters: Parameters)  {

    if (changedParameters.contains('text)) {
      view.setText( parameters.getString( 'text, "") )
    }

  }

}

