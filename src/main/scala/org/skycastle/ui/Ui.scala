package org.skycastle.ui


import content.composite.{CompositePart, CompositeEntity}
import javax.swing.JComponent
import util.{ErrorPrinter, Parameters}
/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
abstract class Ui extends CompositePart {



  /**
   * Define in child classes as the type of JComponent used.
   */
  type ViewType <: JComponent

  @transient
  private var view: ViewType =  null.asInstanceOf[ViewType]


  override def onChildAdded(child: CompositePart, composite: CompositeEntity) {
    if (view != null) {

      // Get or create child view, and add it to this component
      val childView = child.asInstanceOf[Ui].getView(composite)

      view.add(childView)
      view.validate
    }
  }

  override def onChildRemoved(child: CompositePart, composite: CompositeEntity) {

    if (view != null && child.asInstanceOf[Ui].hasView) {
      // Detach child ui
      view.remove( child.asInstanceOf[Ui].getView(composite) )
      view.validate
    }
  }

  override def onUpdate(changedParameters: Parameters, composite: CompositeEntity) {
    if (view != null) {
      updateCommonSwingProperties( view, changedParameters )
      updateViewProperties( view, changedParameters )
    }
  }


  final def hasView = view != null

  final def getView(composite: CompositeEntity): ViewType = {
    if (view == null) {
      view = createView(composite)
    }

    view
  }

  private final def createView(composite: CompositeEntity): ViewType = {
    // Create self
    val view : ViewType = try {
      createOwnView()
    } catch {
      case e : Exception => {
        composite.logWarning( "Error when creating Ui component.  Component: " + id + " ("+this+"),  parameters: " + parameters + ", error: " + e )
        null.asInstanceOf[ViewType]
      }
    }

    if (view != null) {
      // Update it with the initial parameters
      updateViewProperties( view, parameters )

      // Create children
      getChildren foreach { c =>
        composite.getComponent(c) match {
          case Some(childComponent : Ui) => {
            val childView = childComponent.getView(composite)
            view.add( childView )
          }
          case None =>
        }
      }
    }

    view
  }


  /**
   * Called to create a swing component for a Ui.  The onUpdate is called afterwards for applying the initial properties.
   */
  def createOwnView() : ViewType

  /**
   * Called when the view properties should be updated
   */
  protected def updateViewProperties(view : ViewType, changedParameters: Parameters)


  private final def updateCommonSwingProperties( component : ViewType, changedParameters: Parameters ) {
    component.setToolTipText( changedParameters.getString( 'tooltip, component.getToolTipText ) )
  }

}

