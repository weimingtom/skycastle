package org.skycastle.entity.entitycontainer.darkstar

import _root_.org.skycastle.util.Parameters
import com.sun.sgs.app.Task

/**
 * A Darkstar Task that calls an action on an entity.
 */
@serializable
@SerialVersionUID( 1 )
case class ActionCallTask( callerId : EntityId, entityId : EntityId, action : Symbol, parameters : Parameters ) extends Task {

  @throws(classOf[Exception])
  def run {
    DarkstarEntityContainer.getEntity( entityId ) match {
      case Some( entity : Entity ) => entity.call( callerId, action, parameters )
      case None => EntityLogger.logWarning( "Entity "+callerId +" could not call action "+action+" on entity "+entityId+", the entity was not found." ) 
    }
  }
}