package org.skycastle.ui


import components.{ButtonUi, PanelUi, LabelUi}
import entity.{EntityId, ArchetypeId, Entity}
import javax.swing.{JLabel, JComponent}
import util.Parameters


/**
 * A set of UI:s laid out into an ui screen.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ScreenEntity(archetype: ArchetypeId) extends Entity(archetype) {

  private var uiComponents : Map[Symbol, Ui] = Map[Symbol, Ui]()



/*
  def button(id: Symbol, parent: Symbol, text: String, tooltip: String,
             targetEntity: EntityId, functionName: String, parameterSources: Map[Symbol, Symbol]) {
  }

  def panel(id: Symbol, parent: Symbol, text: String, tooltip: String, migLayout: String) {

  }

  def tabs(id: Symbol, parent: Symbol, text: String, tooltip: String) {

  }

  def label(id: Symbol, parent: Symbol, text: String, tooltip: String) {

  }

  def checkbox(id: Symbol, parent: Symbol, text: String, tooltip: String) {

  }

  def entityUi(id: Symbol, parent: Symbol, text: String, tooltip: String, entityId: EntityId) {

  }
*/

  def addUiComponent(componentType: Symbol, id: Symbol, parent: Symbol, parameters: Parameters) {
    // Remove any previous component with same id
    if (uiComponents.contains( id )) {
      removeUiComponent( id )
    }

    // Create UiComponent and add
    val ui: Ui = createUiOfType( componentType, id, parent, parameters)
    if (ui != null) {
      ui.init( id, parent, parameters )
      val entry = (id, ui)
      uiComponents = uiComponents + entry

      // Add to parent
      uiComponents.get(parent) match {
        case Some(parentComponent) => {
          parentComponent.addChild(ui, uiComponents)
        }
        case None =>
      }
    }
  }

  private def createUiOfType( componentType : Symbol, id: Symbol, parent: Symbol, parameters: Parameters ) : Ui = {
    componentType match {
      case 'label => new LabelUi()
      case 'panel => new PanelUi()
      case 'button => new ButtonUi()
    }


  }


  def updateUiComponent(id: Symbol, parameters: Parameters) {
    uiComponents.get(id) match {
      case Some(component) => {
        component.update(parameters)
      }
      case None =>
    }
  }

  def removeUiComponent(id: Symbol) {
    uiComponents.get(id) match {
      case Some(ui) => {
        ui.remove(uiComponents)
        uiComponents = uiComponents - id
      }
      case None =>
    }
  }


  def invoke(actionName: String, parameters: Parameters) = null

  def createUi(parameters: Parameters) : Ui = {
    val rootUiId = parameters.get[Symbol]( 'rootComponentId, 'root )

    uiComponents.getOrElse(rootUiId, null)
  }

  def getView() : JComponent = {

    val ui : Ui = createUi(properties)
    ui.getView( uiComponents )
  }

}

