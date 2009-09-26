package org.skycastle.content.activities.textedit


import entity.EntityId
import network.Transferable

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
abstract class TextBlock( author : EntityId ) extends Transferable {

  def addText( text : String )
  def insertText( index : Int, text : String )
  def insertBlock( index : Int, block : TextBlock )


  def toTransferObject = {


  }

}