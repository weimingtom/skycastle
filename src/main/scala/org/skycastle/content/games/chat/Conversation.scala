package org.skycastle.content.games.chat


import entity.{EntityId, Entity}
import util.Parameters

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
// TODO: Some way to listen to the answer to a specific message?  Some kind of sender specified message id?
class Conversation extends Entity {

  private var members : Map[ String, EntityId ] = Map()

  def join( caller : EntityId, nickname : String ) {

    // Need to reply to a call?  With error condition, or acceptance condition?

    // TODO: Use a Parameters instance as parameter?
    // Allows easy addition of parameters later, and also simplifies reflection based dispatch
    // Maybe autodetect when reflecting

    // TODO: Make sure the nickname is an accepted identifier.

    if ( !members.contains(nickname) ) {
      val entry = ( nickname, caller )
      members = members + entry

      // Send join ok message
      // TODO: Add conversation topic / name or such?
      callOtherEntity( caller, 'conversationMessage, Parameters( 'type -> 'joinedConversation ) )

      // TODO: Notify everyone about the join
    }
    else {
      callOtherEntity( caller, 'conversationMessage, Parameters( 'type -> 'error, 'message -> "The nickname is already in use." ) )
    }

  }

  def say( caller : EntityId, nickname : String, message : String ) {
    members.get( nickname ) match {
      case Some( user ) => {
        if (user == caller) {

          members.values foreach { memberId : EntityId =>
            callOtherEntity( memberId, 'conversationMessage, Parameters( 'type -> 'message, 'from -> nickname, 'message -> message ) )
          }
        }
        else {
          callOtherEntity( caller, 'conversationMessage, Parameters( 'type -> 'error, 'message -> "Not a member of the conversations, or is not you." ) )
        }
      }
      case None => {
        callOtherEntity( caller, 'conversationMessage, Parameters( 'type -> 'error, 'message -> "Not a member of the conversations, or is not you." ) )
      }
    }
  }

  def leave( caller : EntityId, nickname : String, message : String ) {
    members.get( nickname ) match {
      case Some( user ) => {
        if (user == caller) {
          members = members - nickname
          // TODO: Add conversation topic / name or such?
          callOtherEntity( caller, 'conversationMessage, Parameters( 'type -> 'leftConversation ) )

          // TODO: Notify everyone about the leave
        }
        else {
          callOtherEntity( caller, 'conversationMessage, Parameters( 'type -> 'error, 'message -> "Not a member of the conversations, or is not you." ) )
        }
      }
      case None => {
        callOtherEntity( caller, 'conversationMessage, Parameters( 'type -> 'error, 'message -> "Not a member of the conversations, or is not you." ) )
      }
    }
  }


}

