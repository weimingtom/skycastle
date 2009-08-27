package org.skycastle.ui


import javax.swing.{JLabel, JComponent}
import util.{ErrorPrinter, Parameters}
/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
abstract case class Ui(childrenSupported : Boolean) {

  /**
   * Define in child classes as the type of JComponent used.
   */
  type ViewType <: JComponent

  var id: Symbol = null
  var parent: Symbol = null
  var parameters: Parameters = null

  private var children: List[Symbol] = Nil

  @transient
  private var view: ViewType =  null.asInstanceOf[ViewType]

  final def init( id_ : Symbol, parent_ : Symbol, parameters_ : Parameters) {
    id = id_
    parent = parent_
    parameters = parameters_
    onInit()
  }

  final def getChildren = children

  final def addChild(child: Ui, components: Map[Symbol, Ui]) {
    if (childrenSupported) {
      if (!children.contains(child.id)) children = children ::: List( child.id )

      if (view != null) {

        // Get or create child view, and add it to this component
        val childView = child.getView(components)
        
        view.add(childView)
        view.validate
      }
    }
  }

  final def removeChild(child: Ui, components: Map[Symbol, Ui]) {
    if (childrenSupported) {
      if (children.contains(child.id)) {
        children = children.remove(_ == child.id)

        if (view != null && child.hasView) {
          // Detach child ui
          view.remove( child.getView(components) )
          view.validate
        }
      }
    }
  }


  final def hasView = view != null

  final def getView(components: Map[Symbol, Ui]): ViewType = {
    if (view == null) {
      view = createView(components)
    }

    view
  }

  private final def createView(components: Map[Symbol, Ui]): ViewType = {
    // Create self
    val view : ViewType = try {
      createOwnView()
    } catch {
      case e : Exception => {
        UiLogger.logger.warning( "Error when creating Ui component.  Component: " + id + " ("+this+"),  parameters: " + parameters + ", error: " + e )
        null.asInstanceOf[ViewType]
      }
    }

    if (view != null) {
      // Update it with the initial parameters
      updateProperties( view, parameters )

      // Create children
      children foreach { c =>
        components.get(c) match {
          case Some(childComponent) => {
            val childView = childComponent.getView(components)
            view.add( childView )
          }
          case None =>
        }
      }
    }

    view
  }


  final def update(changedParameters: Parameters) {
    parameters.update(changedParameters)

    if (view != null) {
      updateProperties( view, changedParameters )
    }
  }


  final def remove(components: Map[Symbol, Ui]) {

    onRemove()

    // Remove the child components
    children foreach { c =>
      components.get(c) match {
        case Some(childComponent) => childComponent.remove(components)
        case None =>
      }
    }

    // Remove this from parent
    components.get(parent) match {
      case Some(parentComponent) => parentComponent.removeChild(this, components)
      case None =>
    }

    children = Nil
    parameters = null
  }

  private final def updateProperties(component : ViewType, changedParameters: Parameters){
    try {
      updateCommonSwingProperties( component, changedParameters )
      onUpdate(component, changedParameters)
    } catch {
      case e : Exception => UiLogger.logger.warning( ErrorPrinter.prettyPrint("Error when updating parameters for Ui component.  Update partially applied.  Component: " + id + " ("+component+"),  updated parameters: " + changedParameters, e ) )
    }
  }

  private final def updateCommonSwingProperties( component : ViewType, changedParameters: Parameters ) {

    component.setToolTipText( changedParameters.getString( 'tooltip, component.getToolTipText ) )

  }

  /**
   * Called after a Ui is created, but before the view has been created.
   */
  def onInit() {}

  /**
   * Called to create a swing component for a Ui.  The onUpdate is called afterwards for applying the initial properties.
   */
  def createOwnView() : ViewType

  /**
   *  Called when a component has been created with the initial parameters, and whenever it is updated with the changed parameters.
   */
  def onUpdate( view : ViewType, changedParameters: Parameters )

  /**
   * Called before a component is removed.
   */
  def onRemove() {}
}

