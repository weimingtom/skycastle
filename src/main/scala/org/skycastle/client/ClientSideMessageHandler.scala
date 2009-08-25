package org.skycastle.client



import entity.ArchetypeId
import util.Parameters

/**
 * Receives messages from the server and handles them.
 * 
 * @author Hans Haggstrom
 */

abstract class ClientSideMessageHandler {

  def addArchetype( archetypeId : ArchetypeId, archetypeKind : String, parameters : Parameters )

  def updateArchetype( archetypeId : ArchetypeId, updateType : String, parameters : Parameters )


  def createEntity( entityId : Long, archetypeId : ArchetypeId, parameters : Parameters )

  def updateEntity( entityId : Long, updateType : String, parameters : Parameters )

  def removeEntity( entityId : Long )

  
  def openUi( entityId : Long )

}

