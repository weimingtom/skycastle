package org.skycastle.ui.components


import content.composite.{CompositePart, CompositeEntity}
import entity.EntityId
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.JButton
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
// TODO: Make action parameters be some expression objects that calculate the parameter value in some way - either constant value,
// TODO: or a value of some Ui component, or a value of a property.  Or possibly a value of a property of another entity?

@serializable
@SerialVersionUID(1)
class ButtonUi extends Ui {
  type ViewType = JButton

  var calledEntity : EntityId = null
  var calledAction : Symbol = null
  var actionParameters : Map[ Symbol, Symbol ] = Map()


  def createOwnView(composite: CompositeEntity) : ViewType = {
    val view = new JButton()
    view.addActionListener( new ActionListener {
      def actionPerformed(e: ActionEvent) = {
        composite.callOtherEntity( calledEntity, calledAction, collectParameters(composite) )
      }
    })
    
    view
  }

  private def collectParameters(composite: CompositeEntity) : Parameters = {

    var params : Map[Symbol, Object] = Map()

    actionParameters.keys foreach { key : Symbol =>
      val sourceComponent = actionParameters(key)
      composite.getComponent( sourceComponent ) match {
        case Some( comp : Ui ) => {
          val value = comp.getValue()
          val entry = ( key, value )
          params = params + entry
        }
        case _ =>
      }
    }

    new Parameters(params)
  }


  protected def updateViewProperties(view: ViewType, changedParameters: Parameters)  {
    if (changedParameters.has('text))
      view.setText( changedParameters.getString('text, "") )

    if (changedParameters.has('calledEntity))
      calledEntity = changedParameters.getAs[EntityId]('calledEntity, null)

    if (changedParameters.has('calledAction))
      calledAction = changedParameters.getAs[Symbol]('calledAction, null)
    
    if (changedParameters.has('actionParameters))
      actionParameters = changedParameters.getAs[Map[Symbol,Symbol]]('actionParameters, Map[Symbol,Symbol]())

  }
}

