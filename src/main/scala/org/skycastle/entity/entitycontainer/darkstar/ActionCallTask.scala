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

    val headEntityId = entityId.headEntityId
    DarkstarEntityContainer.getEntity( headEntityId ) match {
      case Some(entity : Entity) => {
        entityId.tailEntityId match  {
          case Some( innerEntityId ) => entity.callContained( callerId, entityId, action, parameters )
          case None => entity.call( callerId, action, parameters )
        }
      }
      case None => EntityLogger.logWarning( "Entity "+callerId +" could not call action "+action+" on entity "+entityId+", the entity was not found." )
    }
  }
}