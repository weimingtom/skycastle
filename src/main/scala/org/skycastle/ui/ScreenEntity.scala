package org.skycastle.ui


import components.{FieldUi, LabelUi, PanelUi, ButtonUi}
import org.skycastle.content.composite.CompositeEntity
import org.skycastle.entity.Entity
import java.awt.Dimension
import javax.swing.{JLabel, JFrame, JComponent}
import org.skycastle.util.PropertyGetters

/**
 * A set of UI:s laid out into an ui screen.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ScreenEntity extends CompositeEntity {

  @transient
  private var frame : JFrame = null

  type COMPONENT = Ui
  type VIEW = JComponent
  val componentClass = classOf[Ui]

  override protected def createComponentOfType(componentType: String, componentId: Symbol) : COMPONENT = {
    componentType match {
      case "label" => new LabelUi()
      case "panel" => new PanelUi()
      case "button" => new ButtonUi()
      case "field" => new FieldUi()
      case _ => null.asInstanceOf[COMPONENT]
    }

  }

  def getRootUi : Ui = {
    getComponent( 'root ) match {
      case Some(x : Ui) => x
      case _ => null
    }
  }

  def getView(): JComponent = {

    val ui: Ui = getRootUi
    if (ui == null) new JLabel( "No root UI found." )
    else ui.getView(this)
  }

  def showFrame() {

    if (frame != null)
    {
        frame.setVisible(true)
    }
    else {
      frame = new JFrame()

      val rootUi = getRootUi
      if (rootUi != null) {
        frame.setTitle( rootUi.parameters.getString( 'text, "Skycastle" ) )
        // TODO: Icon
      }
  
      // TODO: Listen to close, possibly ask for permission, then shutdown the client
      frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )

      frame.getContentPane.add( getView() )

      // TODO: Get default size as parameter?
      // TODO: Also add set / reset / toggle fullscreen options, callable on the client entity.
      frame.setPreferredSize( new Dimension( 1200, 800 ) )

      frame.pack

      frame.setVisible( true )
    }
  }

}

