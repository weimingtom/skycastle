package org.skycastle.content.activities.chat


import entity.{EntityId, Entity}
import activity.ActivityEntity
import util.Parameters

/**
 * Simple chat channel implementation.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class Conversation extends ActivityEntity {

  // TODO: This should be a val really -> annotate the class with constructor parameter metadata, and use that when instantiating entities?
  var conversationName : String = ""

  private var memberToNick: Map[EntityId, String] = Map()

  private def updateStatus() {
    setStatus( Parameters( 'name -> conversationName ) )
  }

  override protected def onMemberJoined(member: EntityId, joinParameters: Parameters)  {
    val nickname = joinParameters.getString( 'nick, member.toString )
    if (!memberToNick.values.contains(nickname)) {
      val entry = (member, nickname)
      memberToNick = memberToNick + entry

      sendMsgToAll(Parameters('type -> 'joinedConversation, 'nick -> nickname))
    }
    else {
      sendError(member, "The nickname '" + nickname + "' is already in use.")
      removeMember( member )
    }
  }

  override protected def onMemberleft(member: EntityId, leaveParameters: Parameters) {
    memberToNick.get( member ) match {
      case Some( nick ) => {
        sendMsgToAll(Parameters('type -> 'leftConversation, 'nick -> nick, 'message -> leaveParameters.getString( 'message, null )))
        memberToNick = memberToNick - member
      }
      case None => logWarning( "No nickname found for member " + member )
    }
  }

  def say(caller: EntityId, message: String) {
    memberToNick.get( caller ) match {
      case Some( nick ) => {
        sendMsgToAll(Parameters('type -> 'message, 'nick -> nick, 'message -> message))
      }
      case None => logWarning( "No nickname found for member " + caller )
    }
  }


  private def sendMsg(entity: EntityId, parameters: Parameters) {
    callOtherEntity(entity, 'conversationMessage, parameters.add( 'conversation, conversationName )  )
  }

  private def sendMsgToAll(parameters: Parameters) {
    getMembers foreach { memberId: EntityId =>
      sendMsg(memberId, parameters)
    }
  }

  private def sendError(entity: EntityId, message: String) {
    sendMsg(entity, Parameters('type -> 'error, 'message -> message))
  }

  
}

