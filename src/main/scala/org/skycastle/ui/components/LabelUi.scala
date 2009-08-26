package org.skycastle.ui.components


import javax.swing.{JPanel, JLabel}
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class LabelUi extends Ui(false) {

  type ViewType = JLabel

  def createOwnView() = new JLabel()

  def onUpdate(view: ViewType, changedParameters: Parameters) {

    if (changedParameters.contains('text)) {
      view.setText( parameters.getString( 'text, "") )
    }

  }

}

