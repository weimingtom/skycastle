package org.skycastle.content.activities.activitybrowser


import entity.{EntityId, Entity}
import activity.ActivityEntity
import util.{ Parameters}
import util.ParameterChecker._

/**
 * Allows some user to browse through a list of ongoing games, join one, or start a new one.
 *
 * Used as an entrypoint in a server, but can also be used in-game for sub-games in some place.
 * 
 * @author Hans Haggstrom
 */
// TODO: Switch to EntityFactories for activities instead?  Basically some parametrized activities - same class can e.g. be used both for a small and large go game.
@serializable
@SerialVersionUID(1)
class ActivityBrowser extends ActivityEntity {


  private var activityTypes : Map[String, Parameters] = Map()

  private var activities : Map[EntityId, Parameters] = Map()

  override protected def onMemberJoined(member: EntityId, joinParameters: Parameters) {
    activityTypes foreach { case (activityClass : String, info : Parameters) =>
      notifyMemberOfAddedActivityType( member, activityClass, info )
    }
    activities foreach { case (activityId : EntityId, info : Parameters) =>
      notifyMemberOfUpdate( member, activityId , info )
    }
  }

  def addActivityType( caller : EntityId, activityClass : String, activityInfo : Parameters ) {
    requireNotNull( caller, 'caller )
    requireNotEmpty( activityClass, 'activityClass )
    requireNotNull( activityInfo, 'activityInfo )

    val entry = ( activityClass, activityInfo )
    activityTypes = activityTypes + entry

    getMembers foreach { m => notifyMemberOfAddedActivityType( m, activityClass, activityInfo ) }
  }

  def removeActivityType( caller : EntityId, activityClass : String ) {
    requireNotNull( caller, 'caller )
    requireNotEmpty( activityClass, 'activityClass )

    if (activityTypes.contains( activityClass )) {
      activityTypes = activityTypes.remove( _ == activityFactory )

      getMembers foreach { m => notifyMemberOfRemovedActivityType( m, activityClass ) }
    }
  }

  def createActivity( user : EntityId, activityType : Symbol, activityParameters : Parameters ) {
    requireNotNull( user, 'user )
    requireNotNull( activityType, 'gameType )
    requireNotNull( activityParameters, 'gameParameters )

    // TODO: Check access rights to create the specified type of activity etc

    // Find and create the activity
    // TODO
    val activity : ActivityEntity = null
    val activityId : EntityId = activity.id
    val parameters = Parameters()

    // Listen to status updates from the activity
    activity.addStatusListener( id )

    // Join the user into the activity
    activity.joinActivity( user, activityParameters )

    // Add the activity to the activitylist
    val entry = (activityId, parameters)
      activityParameters = activityParameters + entry

    // and notify our members
    getMembers foreach { member : EntityId => notifyMemberOfUpdate( member, activityId, parameters ) }
  }



  def removeActivity( activityId : EntityId ) {
    requireNotNull( activityId, 'activityId )

    activities.get( activityId ) match {
      case Some( info ) => {
        activities = activities - activityId

        getMembers foreach { member : EntityId => notifyMemberOfRemoval( member, activityId, info ) }
      }
      case None => logWarning( "Attempt to remove non-existing activity '"+activityId+"'." )
    }
  }


  def activityStatusUpdate( activityId : EntityId, parameters : Parameters ) {
    requireNotNull( activityId, 'activityId )
    requireNotNull( parameters, 'parameters )

    activities.get( activityId ) match {
      case Some( oldInfo ) => {
        val newEntry = ( activityId, parameters )
        activities = activities + newEntry

        getMembers foreach { member : EntityId => notifyMemberOfUpdate( member, activityId, parameters ) }
      }
      case None => logWarning( "Status update received from the entity '"+activityId+"', which is not in the Activity list." )
    }
  }

  private def notifyMemberOfUpdate( member : EntityId, activityId : EntityId, activityInfo : Parameters ) {
    callOtherEntity( member, 'activityUpdated, Parameters( 'activityId -> activityId, 'info -> activityInfo ) )
  }

  private def notifyMemberOfRemoval( member : EntityId, activityId : EntityId, endStatus : Parameters ) {
    callOtherEntity( member, 'activityRemoved, Parameters( 'activityId -> activityId, 'info -> endStatus ) )
  }

  private def notifyMemberOfAddedActivityType( member : EntityId, activityClass : String, activityTypeInfo : Parameters ) {
    callOtherEntity( member, 'activityTypeAdded, Parameters( 'activityClass -> activityClass , 'info -> activityTypeInfo ) )
  }

  private def notifyMemberOfRemovedActivityType( member : EntityId, activityClass : String ) {
    callOtherEntity( member, 'activityTypeRemoved, Parameters( 'activityClass  -> activityClass ) )
  }

}

