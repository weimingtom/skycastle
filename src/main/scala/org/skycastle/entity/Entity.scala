package org.skycastle.entity


import annotations.ActionMethod
import collection.mutable.{MultiMap, HashMap}
import com.sun.sgs.app.ManagedObject
import entitycontainer.CallerId
import javax.swing.JComponent
import ui.Ui
import util.Parameters

/**
 * Represents some object in the game (server or client or both sides).
 *
 * Mutable, can be updated with the update method (usually used for server to client updated).
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
abstract class Entity {

  var id : EntityId = null

  val properties = new Parameters()

  val actions = Map[String, Action]()

  // TODO: Use something more compact than a multimap, or initialize it to small default sizes.
  lazy val capabilities : MultiMap[CallerId, Capability] = new HashMap[CallerId, scala.collection.mutable.Set[Capability]]() with MultiMap[CallerId, Capability]

  /**
   * Update the properties of this Entity
   */
  @ActionMethod
  def updateProperties( updatedProperties : Parameters ) {
    properties.update( updatedProperties )
  }


  // Special actions:
  // * Remove self
  // * Add / remove capabilities to specific callerId
  // * Update properties
  // * Add / remove / change action



  /**
   * Invoke an action available in this entity.
   */
  def invoke( caller : CallerId, actionName : String, parameters : Parameters ) {
    
  }


  /* *
   * Creates an user interface for viewing / invoking actions of this Entity.
   * The parameters can provide additional configuration information for the UI.
   */
//  def createUi( parameters : Parameters ) : Ui

}


