package org.skycastle.ui.components


import entity.EntityId
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JButton
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class ButtonUi extends Ui( false ){
  type ViewType = JButton

  var invokedEntity : EntityId = null
  var invokedMethod : String = null
  var parameterMapping : Map[ Symbol, Symbol ] = Map()

  def createOwnView() : ViewType = {
    val view = new JButton()
    view.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) = {
        // TODO: Implement invocation of the correct method on the correct entity
        println( "TODO: Should now call method " +invokedMethod+ " on entity "+invokedEntity+" with parameters mapped from ui input widgets with the map: " +parameterMapping )
      }
    })
    
    view
  }

  def onUpdate(view: ViewType, changedParameters: Parameters) {
    if (changedParameters.contains('text))
      view.setText( changedParameters.getString('text, "") )

    if (changedParameters.contains('invokedEntity))
      invokedEntity = changedParameters.getAs[EntityId]('invokedEntity, null)

    if (changedParameters.contains('invokedMethod))
      invokedMethod = changedParameters.getString('invokedMethod, null)
    
    if (changedParameters.contains('parameterMapping))
      parameterMapping = changedParameters.getAs[Map[Symbol,Symbol]]('parameterMapping, Map[Symbol,Symbol]())

  }
}

