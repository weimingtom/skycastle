package org.skycastle.content.activities.textedit


import activity.ActivityEntity
import entity.accesscontrol.users
import entity.{EntityId, parameters}
import util.Parameters
/**
 * Simple Activity for editing a text collaboratively.
 *
 * For testing out and developing activities with edit history and undo stacks.
 * 
 * @author Hans Haggstrom
 */
abstract class TexteditActivity extends ActivityEntity {

  private var text = null

  override protected def onMemberJoined(member: EntityId, joinParameters: Parameters) = null
  override protected def onMemberleft(member: EntityId, leaveParameters: Parameters) = null

  @users( "editors" )
  @parameters( "$callerId, row, column" )
  def moveCursor( user : EntityId, row : Int, column : Int )

  @users( "editors" )
  @parameters( "$callerId, text" )
  def addText( user : EntityId, text : String )

  @users( "editors" )
  @parameters( "$callerId, length" )
  def removeText( user : EntityId, length : Int )

  @users( "editors" )
  @parameters( "$callerId" )
  def undo( user : EntityId )

  @users( "editors" )
  @parameters( "$callerId" )
  def redo( user : EntityId )

}