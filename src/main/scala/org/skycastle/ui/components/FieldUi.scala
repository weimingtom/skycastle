package org.skycastle.ui.components


import content.composite.CompositeEntity
import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.event.{DocumentEvent, DocumentListener}
import javax.swing.JTextField
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
import org.skycastle.util.Properties

@serializable
@SerialVersionUID(1)
class FieldUi extends Ui {

  type ViewType = JTextField


  override def getValue() = parameters.getString( 'text, "" )


  def createOwnView(composite: CompositeEntity) : ViewType =  {
    val field = new JTextField( 20 )

    // Listen to text change
    field.getDocument.addDocumentListener( new DocumentListener {

      def updateParameters() {
        parameters.set( 'text, field.getText )
      }

      def changedUpdate(e: DocumentEvent) = updateParameters()
      def insertUpdate(e: DocumentEvent) = updateParameters()
      def removeUpdate(e: DocumentEvent) = updateParameters()
    } )

    field
  }

  protected def updateViewProperties(view: ViewType, changedParameters: Parameters)  {

    if (changedParameters.has('text)) {
      view.setText( parameters.getString( 'text, "") )
    }

  }


}