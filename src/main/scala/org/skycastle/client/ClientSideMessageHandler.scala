package org.skycastle.client



import entity.ArchetypeId
import util.Properties

/**
 * Receives messages from the server and handles them.
 * 
 * @author Hans Haggstrom
 */

abstract class ClientSideMessageHandler {

  def addArchetype( archetypeId : ArchetypeId, archetypeKind : String, parameters : Properties )

  def updateArchetype( archetypeId : ArchetypeId, updateType : String, parameters : Properties )


  def createEntity( entityId : Long, archetypeId : ArchetypeId, parameters : Properties )

  def updateEntity( entityId : Long, updateType : String, parameters : Properties )

  def removeEntity( entityId : Long )

  
  def openUi( entityId : Long )

}

