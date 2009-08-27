package org.skycastle.ui


import components.{ButtonUi, PanelUi, LabelUi}
import entity.{EntityId, ArchetypeId, Entity}
import javax.swing.{JLabel, JComponent}
import util.{ErrorPrinter, Parameters}
/**
 * A set of UI:s laid out into an ui screen.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ScreenEntity(archetype: ArchetypeId) extends Entity(archetype) {

  private var uiComponents : Map[Symbol, Ui] = Map[Symbol, Ui]()
  
  def addUiComponent(componentType: Symbol, id: Symbol, parent: Symbol, parameters: Parameters) {
    //UiLogger.logger.fine( "Adding UI component of type " + componentType+ " with id "+id+", parent "+parent+", and parameters " + parameters )

    if (id == null ) {
      UiLogger.logger.warning( "The id was null when trying to add a component to " +this +".  The component parameters were " +parameters +". Ignoring the add." )
      return
    }

    val ui: Ui = createUiOfType( componentType, id, parent, parameters)
    if (ui == null ) {
      UiLogger.logger.warning( "Unknown component type '"+componentType+"' when trying to add a component to " +this +".  The component parameters were " +parameters +". Ignoring the add." )
      return
    }

    // Remove any previous component with same id
    if (uiComponents.contains( id )) {
      UiLogger.logger.info( "Removing previous component '"+id+"' when adding a new component with the same id to " +this +".  The component parameters were " +parameters +"." )
      removeUiComponent( id )
    }

    ui.init( id, parent, parameters )
    val entry = (id, ui)
    uiComponents = uiComponents + entry

    // Add to parent
    uiComponents.get(parent) match {
      case Some(parentComponent) => {
        parentComponent.addChild(ui, uiComponents)
      }
      case None => {
        if (parent != null) UiLogger.logger.warning( "Could not find a parent component named '"+parent+"' when trying to add a component to " +this +".  The component parameters were " +parameters +". Not adding the component to any parent." )
      }
    }
  }

  private def createUiOfType( componentType : Symbol, id: Symbol, parent: Symbol, parameters: Parameters ) : Ui = {
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
        UiLogger.logger.warning( "Component not found '"+id+"' when trying to update a component in " +this +".  The update parameters were " +parameters +". Ignoring the update." )
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
        UiLogger.logger.warning( "Component not found '"+id+"' when trying to remove a component in " +this +".  Ignoring the remove." )
      }
    }
  }


  def invoke(actionName: String, parameters: Parameters) {
    UiLogger.logger.fine( "invoking action " + actionName + " with parameters " + parameters )
    actionName match {

      case "addUiComponent" => {
        val componentType = parameters.getAs[Symbol]('componentType, null)
        val id = parameters.getAs[Symbol]('id, null)
        val parentId = parameters.getAs[Symbol]('parent, null)
        addUiComponent( componentType, id, parentId, parameters )
      }
      case "updateUiComponent" => {
        val id = parameters.getAs[Symbol]('id, null)
        updateUiComponent( id, parameters )
      }
      case "removeUiComponent" => {
        val id = parameters.getAs[Symbol]('id, null)
        removeUiComponent( id )
      }
      case _ => {
        UiLogger.logger.warning( "Action '"+actionName+"' not found when trying to invoke an action in " +this +".  Ignoring the action call." )
      }
    }


  }

  def createUi(parameters: Parameters) : Ui = {
    val rootUiId = parameters.get[Symbol]( 'rootComponentId, 'root )

    uiComponents.getOrElse(rootUiId, null)
  }

  def getView() : JComponent = {

    val ui : Ui = createUi(properties)
    ui.getView( uiComponents )
  }

}

