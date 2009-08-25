package org.skycastle.server


import entity.{Archetype, ArchetypeId}
import util.Parameters

/**
 * Handles messages received from a connected user.
 * 
 * @author Hans Haggstrom
 */
abstract class ServerSideMessageHandler {

  def requestArchetype( id : String )

  def requestArchetypeRevision( archetypeId : ArchetypeId )

  def createArchetype( id : String, archetypeKind : String, parameters : Parameters )

  def updateArchetype( id : String, updateType : String, parameters : Parameters )

  def branchArchetype( id : String, copyId : String )

  def removeArchetype( id : String )


  def invokeEntityAction( entityId : Long, actionId : String, parameters : Parameters )

}

