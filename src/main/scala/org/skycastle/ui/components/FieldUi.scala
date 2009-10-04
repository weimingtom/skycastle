package org.skycastle.ui.components


import java.awt.event.{ActionEvent, ActionListener}
import javax.swing.event.{DocumentEvent, DocumentListener}
import javax.swing.JTextField
import org.skycastle.content.composite.CompositeEntity
import org.skycastle.ui.Ui
import org.skycastle.util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */

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
        parameters.setProperty( 'text, field.getText )
      }

      def changedUpdate(e: DocumentEvent) = updateParameters()
      def insertUpdate(e: DocumentEvent) = updateParameters()
      def removeUpdate(e: DocumentEvent) = updateParameters()
    } )

    field
  }

  protected def updateViewProperties(view: ViewType, changedParameters: Parameters)  {

    if (changedParameters.hasProperty('text)) {
      view.setText( parameters.getString( 'text, "") )
    }

  }


}