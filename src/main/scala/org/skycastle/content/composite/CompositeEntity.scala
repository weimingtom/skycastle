package org.skycastle.content.composite


import org.skycastle.entity.accesscontrol.{users, ActionCapability}
import org.skycastle.entity.{parameters, Entity}
import org.skycastle.entity.Entity
import org.skycastle.util.{PropertyGetters, ClassUtils}

/**
 * An entity that is composed from several parametrized parts of some types, and where some parts
 * again may be composed of child parts.
 *
 *
 * The parts are not entities, but should be serializable.
 *
 * The parts are instantiated from classes in a specific package, or from a set of listed classes,
 * that should implement some specified trait / interface.
 *
 * The CompositeEntity can also optionally create some kind of result object using the parts (e.g. Swing UI or 3D model).
 * The result object may be updated by the parts as the they are updated, added, and removed, or the result object
 * can be a separate created object or even Entity.
 *
 * @author Hans Haggstrom
 */
// IDEA: May also store editing history by keeping a list with the edit calls done.  This would allow easy replication of the whole object, as well as version history, etc.
// But it can get quite overkill if the object is cloned a lot
//  -> maybe better if it is used more in a factory capacity
//  -> create an Orc Appearance with these parameters (and hook up the control and animation of it to this entity)

// IDEA: Maybe send failure log messages to the calling entity, allowing it to show them e.g. in a feedback messages view in a developers client?
// -> Makes troubleshooting much easier

// TODO: Should this be implemented as an EntityContainer?
@serializable
@SerialVersionUID( 1 )
abstract class CompositeEntity extends Entity {

  private var components: Map[Symbol, COMPONENT] = Map[Symbol, COMPONENT]()

  type COMPONENT <: CompositePart

  val componentClass : Class[COMPONENT]

  addRole( 'editor )

  def getComponent( id : Symbol ) : Option[COMPONENT] = components.get( id )

  @users( "editor" )
  @parameters( "componentType, id, parent, $parameters" )
  def addComponent( componentType: String, id: Symbol, parent: Symbol, parameters: PropertyGetters ) {
    //logTrace( "Adding component of type " + componentType+ " with id "+id+", parent "+parent+", and parameters " + parameters )

    if (componentType == null) {
      logWarning("The componentType was null when trying to add a component.  The component parameters were " + parameters + ". Ignoring the add.")
      return
    }

    if (id == null) {
      logWarning("The id was null when trying to add a component.  The component parameters were " + parameters + ". Ignoring the add.")
      return
    }

    if (components.contains( id )) {
      logWarning("There is already a component with the id '"+id+"'.  Ignoring the add.")
      return
    }

    val component: COMPONENT = createComponentOfType(componentType, id)
    if (component != null) {

      val entry = (id, component)
      components = components + entry
      component.init(id, parent, parameters.toParameters, this)

      // Add to parent
      components.get(parent) match {
        case Some(parentComponent : CompositePart) => {
          parentComponent.addChild(component, this)
        }
        case None => {
          if (parent != null) logWarning("Could not find a parent component named '" + parent + "' when trying to add the component "+id+".  " +
                                         "The component parameters were " + parameters + ". Not adding the component to any parent.")
        }
      }
    }

  }

  @users( "editor" )
  @parameters( "id, $parameters" )
  def updateComponent( id: Symbol, parameters: PropertyGetters ) {
    components.get(id) match {
      case Some(component : CompositePart) => {
        component.update(parameters.toParameters, this)
      }
      case None => {
        logWarning("Component '" + id + "' not found when trying to update it.  The update parameters were " + parameters + ". Ignoring the update.")
      }
    }
  }

  @users( "editor" )
  @parameters( "id" )
  def removeComponent( id: Symbol ) {
    components.get(id) match {
      case Some(component : CompositePart) => {
        component.remove(this)
        components = components - id
      }
      case None => {
        logWarning("Component '" + id + "' not found when trying to remove it.  Ignoring the remove.")
      }
    }
  }
  
  protected def createComponentOfType(componentType: String, componentId : Symbol): COMPONENT = {
    try {
      ClassUtils.createObject( componentType, componentClass )
    }
    catch {
      case e => logWarning("Could not create component of type '" + componentType + "' when trying to add component '"+componentId+"'.  Ignoring the add.", e)
                null.asInstanceOf[COMPONENT]
    }
  }


}

