package org.skycastle.entity


import com.sun.sgs.app.ManagedObject
import ui.Ui
import util.Properties

/**
 * Represents some object in the game (server or client or both sides).
 *
 * Mutable, can be updated with the update method (usually used for server to client updated).
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
abstract class Entity( archetype : ArchetypeId ) {

  val properties = new Properties()

  /**
   * Update this entity
   */
  def update( updateType : String, parameters : Properties ) {
    // TODO: Better handling (allow registering update methods from derived classes), debug log unknown update type
    if (updateType == "properties") updateProperties( parameters )
  }

  def updateProperties( parameters : Properties ) {
    properties.update( parameters )
  }

  /**
   * Invoke an action available in this entity.
   */
  def invoke( actionName : String, parameters : Properties )


  /**
   * Creates an user interface for viewing / invoking actions of this Entity.
   * The parameters can provide additional configuration information for the UI.
   */
  def createUi( parameters : Properties ) : Ui

}


