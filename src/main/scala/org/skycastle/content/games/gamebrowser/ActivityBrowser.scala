package org.skycastle.content.games.gamebrowser


import entity.{EntityId, Entity}
import util.Parameters
import game.ActivityEntity

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

  var activities : List[EntityId] = Nil

  override protected def onMemberJoined(member: EntityId, joinParameters: Parameters) {
    // Send activity list to member
    callOtherEntity( member, 'activityList, Parameters( 'activities -> activities  ) )
  }

  def createActivity( user : EntityId, gameType : Symbol, gameParameters : Parameters ) {
    // TODO: Check access rights to create the specified type of game etc

    // Find and create the game
    // TODO

    // Listen to status updates from the game
    // TODO

    // Join the user into the game
    // TODO

    // Add the game to the gamelist, and notify our members
    // TODO
  }


}

