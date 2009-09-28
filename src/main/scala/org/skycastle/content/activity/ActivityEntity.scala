package org.skycastle.content.activity


import entity.accesscontrol.{users, RoleMembersFunction, RoleMember}
import entity.{parameters, EntityId, Entity}
import util.{Properties, Parameters}
/**
 * An activity is something users can join into and experience together with others.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ActivityEntity extends Entity {

  private var members : List[EntityId] = Nil
  private var status : Parameters = Parameters()
  private var statusListeners : List[EntityId] = Nil

  // Add users used for members of the activity
  addRole( 'activityMember )
  addRoleMember( 'activityMember, RoleMembersFunction( entity => members.contains( entity ) ))

  /**
   * Calls back with avatar entity id? Or just confirmation.
   */
  @users("everyone")
  @parameters("$callerId, $parameters")
  final def joinActivity( caller : EntityId, parameters : Parameters ) {
    if (!hasMember( caller )) {
      addMember( caller )
      callOtherEntity( caller, 'joinedActivity, status )
      onMemberJoined( caller, parameters )
      createClientUi( caller )
    }
  }

  /**
   * Removes a member from the activity.
   */
  @users("activityMember")
  @parameters("$callerId, $parameters")
  final def leaveActivity( caller : EntityId, parameters : Parameters  ) {
    if (hasMember( caller )) {
      removeMember( caller )
      callOtherEntity( caller, 'leftActivity, status )
      onMemberleft( caller, parameters )
    }
  }

  /**
   * Builds up a client side view of the activity.
   *
   * Can also be called after the client has already joined and participated in the action,
   * so that the client can resume after reconnection, etc.
   */
  @users("activityMember")
  @parameters("$callerId" )
  def createClientUi( client : EntityId ) {}

  /**
   * Adds a listener that is notified about status changes of the activity (e.g. if new users can join, the final outcome when the acticity ends, etc)
   *
   * Immediately after starting listening the status listener is notified about the current state.
   */
  @users("activityMember")
  @parameters("$callerId")
  final def addStatusListener( listener : EntityId ) {
    if (!statusListeners.contains( listener )) {
      statusListeners = listener :: statusListeners
      notifyStatusListener( listener )
    }
  }

  /**
   * Removes the listener.
   */
  @users("activityMember")
  @parameters("$callerId")
  final def removeStatusListener( listener : EntityId ) {
    statusListeners = statusListeners.remove( _ == listener )
  }


  /**
   * The current status of the activity.
   */
  final def getStatus() : Parameters = status

  /**
   * Updates the status and notifiers listener.
   */
  final protected def setStatus( newStatus : Parameters ) {
    status = newStatus.add( 'activityId, id ) // Also keep the id of the activity in its status, for reference.
    statusListeners foreach notifyStatusListener
  }

  /**
   * The entities that are currently joined into the Activity.
   */
  final protected def getMembers : List[EntityId] = members

  /**
   * True if this Activity has the specified member.
   */
  final protected def hasMember( member : EntityId ) : Boolean = members.contains( member )

  /**
   * Adds the specified member.
   * Doesn't call onMemberJoined.
   */
  final protected def addMember( member : EntityId ) {
    members = members ::: List( member )
  }

  /**
   * Remove the specified member.
   * Doesn't call onMemberLeft
   */
  final protected def removeMember( member : EntityId ) {
    members = members.remove( _ == member )
  }

  /**
   * Called when a new member joined this activity, passing in some specified parameters.
   */
  protected def onMemberJoined( member : EntityId, joinParameters : Parameters ) {}

  /**
   * Called when a member left this activity.
   */
  protected def onMemberleft( member : EntityId, leaveParameters : Parameters  ) {}


  private def notifyStatusListener( listener : EntityId ) {
    callOtherEntity( listener, 'activityStatusUpdate, status )
  }
}

