package org.skycastle.server


import com.sun.sgs.app.{AppContext, ManagedObject}
import entity.{EntityId, Entity}
/**
 * ManagedObject wrapper for an Entity
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class ManagedEntity[T <: Entity]( entity : T ) extends ManagedObject {

  initialize()

  private def initialize() {

    val dataManager = AppContext.getDataManager
    val id = dataManager.getObjectId( this )

    val entityIdPrefix = "entity-"
    val name = entityIdPrefix + id.toString

    entity.id = new EntityId(name)

    dataManager.setBinding( entity.id.managedObjectName, entity )
  }
}

