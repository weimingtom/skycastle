package org.skycastle.content.composite

import org.skycastle.util._

/**
 * A part of a CompositeEntity.
 * 
 * @author Hans Haggstrom
 */
class CompositePart {

  /**
   * Whether child components can be added to this component.   Override if necessary.
   */
  val childrenSupported : Boolean = false

  var id: Symbol = null
  var parent: Symbol = null
  var parameters: SimpleProperties = null

  private var children: List[Symbol] = Nil


  /**
   * Called just after a component has been created, and before it is added to any parent component.
   */
  def onInit(parameters: SimpleProperties, composite : CompositeEntity) {}

  /**
   * Called when a component is updated with changed parameters.
   * (after the parameters var has been updated)
   */
  def onUpdate( changedParameters: Parameters, composite : CompositeEntity ) {}

  /**
   * Called as a component is removed.
   * (After its children have been removed, but after it is removed from its parent).
   */
  def onRemove(composite : CompositeEntity) {}

  /**
   * Called when a child component has been added.
   */
  def onChildAdded(child: CompositePart, composite : CompositeEntity) {}

  /**
   * Called before a child component is removed.
   */
  def onChildRemoved(child: CompositePart, composite : CompositeEntity) {}



  final def init( id_ : Symbol, parent_ : Symbol, parameters_ : Parameters, composite : CompositeEntity) {
    id = id_
    parent = parent_
    parameters = new SimpleProperties( parameters_ )

    try {
      onInit(parameters, composite)
    }
    catch {
      case e : Throwable => composite.logError( "Exception when initializing component "+id+" : " + e, e )
    }
  }

  final def getChildren = children


  final def addChild(child: CompositePart, composite : CompositeEntity) {
    if (!childrenSupported) {
      composite.logWarning( "Could not add child '"+child.id+"' to component '"+id+"' - it doesn't accept children." )
    }
    else {
      if (children.contains(child.id)) {
        composite.logWarning( "Could not add child '"+child.id+"' to component '"+id+"' - it is already added." )
      }
      else {
        children = children ::: List( child.id )

        try {
          onChildAdded(child, composite)
        }
        catch {
          case e : Throwable => composite.logError( "Exception when adding child component "+child+" to "+id+": " + e, e )
        }
      }
    }
  }

  final def removeChild(child: CompositePart, composite : CompositeEntity) {
    if (childrenSupported && children.contains(child.id)) {

      try {
        onChildRemoved(child, composite)
      }
      catch {
        case e : Throwable => composite.logError( "Exception when removing child component "+child.id+" from "+id+": " + e, e )
      }

      children = children.remove(_ == child.id)
    }
    else {
      composite.logWarning( "Could not find the child when trying to remove child component '"+child.id+"' from component '"+id+"'." )
    }
  }

  


// TODO: Rename to avoid overloading with scala specified update method
  final def update(changedParameters: Parameters, composite : CompositeEntity) {
    parameters.setProperties(changedParameters)

    try {
      onUpdate(changedParameters,  composite)
    }
    catch {
      case e : Throwable => composite.logError( "Exception when updating component "+id+" with parameters '"+parameters+"': " + e, e )
    }
  }


  final def remove(composite : CompositeEntity) {

    // Remove the child components
    children foreach { c => composite.removeComponent( c ) }

    try {
      onRemove(composite)
    }
    catch {
      case e : Throwable => composite.logError( "Exception when removing component "+id+": " + e, e )
    }


    // Remove this from parent
    composite.getComponent( parent ) match {
      case Some(parentComponent) => parentComponent.removeChild(this, composite)
      case None =>
    }

    children = Nil
    parameters = null
    parent = null
  }



}

