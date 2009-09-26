package org.skycastle.content.activity


import entity.accesscontrol.users
import entity.{parameters, EntityId, Entity}
import util.Parameters

/**
 * A client side entity for interfacing with an Activity.
 *
 * Shows activity UI:s to the user, and sends user input to the activity.
 *
 * Takes activity ID to join as an initialization parameter (activityId).
 */
@serializable
@SerialVersionUID(1)
class ActivityClient() extends Entity {

  private var connectedActivityId : EntityId = null

  addRole('activity)

  override protected final def onInit(initParameters: Parameters) {
    val activityId : EntityId = initParameters.getEntityId( 'activityId, null )
    if (activityId == null) {
      logError( "activityId was null, removing self from container." )
      container.removeEntity( id ) // Suicide, to avoid littering container with broken entities.
    }
    else {
      connectedActivityId = activityId
      addRoleMember( 'activity, activityId )
      onCreated( initParameters )
      callOtherEntity( activityId, 'joinActivity, Parameters() )
    }
  }

  @users( "activity" )
  @parameters("$callerId, $parameters")
  final def joinedActivity( activityId : EntityId, activityStatus : Parameters ) {
    onJoined(activityStatus)
  }
  
  @users( "activity" )
  @parameters("$callerId, $parameters")
  final def leftActivity( activityId : EntityId, activityStatus : Parameters ) {
    onLeft(activityStatus)
  }

  protected def onCreated( creationParams : Parameters  ) {}
  protected def onJoined( activityStatus : Parameters  ) {}
  protected def onLeft( activityStatus : Parameters  ) {}

}