package org.skycastle.server


import com.sun.sgs.app.{DataManager, AppContext, ManagedObject}
import entity.{ArchetypeId, Archetype}
/**
 * ManagedObject wrapper for an Archetype
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class ManagedArchetype( archetype : Archetype ) extends ManagedObject {

  initialize()

  private def initialize() {

    val dataManager = AppContext.getDataManager
    val id = dataManager.getObjectId( this )

    val archetypeIdPrefix = "archetype-"
    val revision = 1
    val basename = archetypeIdPrefix + id.toString

    archetype.id = new ArchetypeId(basename, revision)

    dataManager.setBinding( archetype.id.managedObjectName, archetype )
  }


}

