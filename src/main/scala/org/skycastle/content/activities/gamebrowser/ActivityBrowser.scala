package org.skycastle.content.activities.gamebrowser


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
@serializable
@SerialVersionUID(1)
class ActivityBrowser extends ActivityEntity {

  var activityParameters : Map[EntityId, Parameters] = Map()

  override protected def onMemberJoined(member: EntityId, joinParameters: Parameters) {
    activityParameters foreach { case (activityId : EntityId, info : Parameters) =>
       notifyMemberOfUpdate( member, activityId , info )
    }
  }

  def createActivity( user : EntityId, gameType : Symbol, gameParameters : Parameters ) {
    requireNotNull( user, 'user )
    requireNotNull( gameType, 'gameType )
    requireNotNull( gameParameters, 'gameParameters )

    // TODO: Check access rights to create the specified type of game etc

    // Find and create the game
    // TODO
    val activity : ActivityEntity = null
    val activityId : EntityId = activity.id
    val parameters = Parameters()

    // Listen to status updates from the game
    activity.addStatusListener( id )

    // Join the user into the game
    activity.joinActivity( user, gameParameters )

    // Add the game to the gamelist
    val entry = (activityId, parameters)
      activityParameters = activityParameters + entry

    // and notify our members
    getMembers foreach { member : EntityId => notifyMemberOfUpdate( member, activityId, parameters ) }
  }



  def removeActivity( activityId : EntityId ) {
    requireNotNull( activityId, 'activityId )

    activityParameters.get( activityId ) match {
      case Some( info ) => {
        activityParameters = activityParameters - activityId

        getMembers foreach { member : EntityId => notifyMemberOfRemoval( member, activityId, info ) }
      }
      case None => logWarning( "Attempt to remove non-existing activity '"+activityId+"'." )
    }
  }


  def activityStatusUpdate( activityId : EntityId, parameters : Parameters ) {
    requireNotNull( activityId, 'activityId )
    requireNotNull( parameters, 'parameters )

    activityParameters.get( activityId ) match {
      case Some( oldInfo ) => {
        val newEntry = ( activityId, parameters )
        activityParameters = activityParameters + newEntry

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

}

