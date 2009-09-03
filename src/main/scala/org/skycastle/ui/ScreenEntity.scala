package org.skycastle.ui


import components.{LabelUi, PanelUi, ButtonUi}
import entity.accesscontrol.ActionCapability
import entity.Entity
import java.awt.Dimension
import javax.swing.{JLabel, JFrame, JComponent}
import util.{Parameters}

/**
 * A set of UI:s laid out into an ui screen.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ScreenEntity extends Entity {
  private var uiComponents: Map[Symbol, Ui] = Map[Symbol, Ui]()

  @transient
  private var frame : JFrame = null

  def addUiComponent(componentType: Symbol, id: Symbol, parent: Symbol, parameters: Parameters) {
    //UiLogger.logger.fine( "Adding UI component of type " + componentType+ " with id "+id+", parent "+parent+", and parameters " + parameters )

    if (id == null) {
      UiLogger.logger.warning("The id was null when trying to add a component to " + this + ".  The component parameters were " + parameters + ". Ignoring the add.")
      return
    }

    val ui: Ui = createUiOfType(componentType, id, parent, parameters)
    if (ui == null) {
      UiLogger.logger.warning("Unknown component type '" + componentType + "' when trying to add a component to " + this + ".  The component parameters were " + parameters + ". Ignoring the add.")
      return
    }

    // Remove any previous component with same id
    if (uiComponents.contains(id)) {
      UiLogger.logger.info("Removing previous component '" + id + "' when adding a new component with the same id to " + this + ".  The component parameters were " + parameters + ".")
      removeUiComponent(id)
    }

    ui.init(id, parent, parameters)
    val entry = (id, ui)
    uiComponents = uiComponents + entry

    // Add to parent
    uiComponents.get(parent) match {
      case Some(parentComponent) => {
        parentComponent.addChild(ui, uiComponents)
      }
      case None => {
        if (parent != null) UiLogger.logger.warning("Could not find a parent component named '" + parent + "' when trying to add a component to " + this + ".  The component parameters were " + parameters + ". Not adding the component to any parent.")
      }
    }
  }

  private def createUiOfType(componentType: Symbol, id: Symbol, parent: Symbol, parameters: Parameters): Ui = {
    componentType match {
      case 'label => new LabelUi()
      case 'panel => new PanelUi()
      case 'button => new ButtonUi()
      case _ => null
    }
  }


  def updateUiComponent(id: Symbol, parameters: Parameters) {
    uiComponents.get(id) match {
      case Some(component) => {
        component.update(parameters)
      }
      case None => {
        UiLogger.logger.warning("Component not found '" + id + "' when trying to update a component in " + this + ".  The update parameters were " + parameters + ". Ignoring the update.")
      }
    }
  }

  def removeUiComponent(id: Symbol) {
    uiComponents.get(id) match {
      case Some(ui) => {
        ui.remove(uiComponents)
        uiComponents = uiComponents - id
      }
      case None => {
        logWarning("Component not found '" + id + "' when trying to remove a component in " + this + ".  Ignoring the remove.")
      }
    }
  }

  def createUiEditorRole() {
    addRole( "uiEditor" )
    addRoleCapability( "uiEditor", ActionCapability( "addUiComponent" ) )
    addRoleCapability( "uiEditor", ActionCapability( "updateUiComponent" ) )
    addRoleCapability( "uiEditor", ActionCapability( "removeUiComponent" ) )
  }

  protected override def callBuiltinAction(actionName: String, parameters: Parameters): Boolean = {
    actionName match {
      case "addUiComponent" => {
        val componentType = parameters.getAs[Symbol]('componentType, null)
        val id = parameters.getAs[Symbol]('id, null)
        val parentId = parameters.getAs[Symbol]('parent, null)
        addUiComponent(componentType, id, parentId, parameters)
        true
      }
      case "updateUiComponent" => {
        val id = parameters.getAs[Symbol]('id, null)
        updateUiComponent(id, parameters)
        true
      }
      case "removeUiComponent" => {
        val id = parameters.getAs[Symbol]('id, null)
        removeUiComponent(id)
        true
      }
      case _ => {
        false
      }
    }
  }

  def getRootUi : Ui = {
    uiComponents.getOrElse('root, null)
  }

  def getView(): JComponent = {

    val ui: Ui = getRootUi
    if (ui == null) new JLabel( "No root UI found." )
    else ui.getView(uiComponents)
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

