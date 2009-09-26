package org.skycastle.entity.entitycontainer.darkstar

import _root_.org.skycastle.util.Parameters
import com.sun.sgs.app.Task
import network.Message

/**
 * A Darkstar Task that calls an action on an entity.
 */
@serializable
@SerialVersionUID( 1 )
case class ActionCallTask( message : Message ) extends Task {

  @throws(classOf[Exception])
  def run {

    val headEntityId = message.calledEntity.headEntityId
    DarkstarEntityContainer.getEntity( headEntityId ) match {
      case Some(entity : Entity) => {
        message.calledEntity.tailEntityId match  {
          case Some( innerEntityId ) => entity.callContained( message )
          case None => entity.call( message )
        }
      }
      case None => EntityLogger.logWarning( "Entity "+message.callingEntity +" could not call action "+message.calledAction+" on entity "+message.calledEntity+", the entity was not found." )
    }
  }
}