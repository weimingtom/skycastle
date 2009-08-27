package org.skycastle.entity.entitycontainer.darkstar

import _root_.org.skycastle.server.ManagedEntity
import com.sun.sgs.app.{DataManager, NameNotBoundException, AppContext}
import java.lang.ClassCastException

/**
 * A singleton object for accessing the DarkstarEntityContainer
 */
object DarkstarEntityContainer extends EntityContainer {


  def storeEntity(entity: Entity) : EntityId = {
    val dataManager : DataManager = AppContext.getDataManager

    val managedEntity = new ManagedEntity( entity )

    val id = EntityId("entity-" + dataManager.getObjectId( managedEntity ).toString)

    managedEntity.entity.id = id
    managedEntity.entity.container = managedEntity

    dataManager.setBinding( id.managedObjectName, managedEntity )

    id
  }

  def markForUpdate( entity : Entity ) {
    if (entity.container != null) {
      AppContext.getDataManager.markForUpdate( entity.container  )
    }
  }

  def getEntity(entityId: EntityId) : Option[Entity] = {
    if (entityId == null) return None
    else try {
      Some[Entity]( AppContext.getDataManager.getBinding( entityId.managedObjectName ).asInstanceOf[ManagedEntity[Entity]].entity )
    } catch {
      case e : NameNotBoundException => None
      case e : ClassCastException => None
    }
  }

  def getEntityForUpdate(entityId: EntityId) : Option[Entity] = {
    if (entityId == null) return None
    else try {
      Some[Entity]( AppContext.getDataManager.getBindingForUpdate( entityId.managedObjectName ).asInstanceOf[ManagedEntity[Entity]].entity )
    } catch {
      case e : NameNotBoundException => None
      case e : ClassCastException => None
    }
  }

  def removeEntity(entityId: EntityId) {
    val dataManager : DataManager = AppContext.getDataManager

    try {
      val managedObject = dataManager.getBinding( entityId.managedObjectName )
      dataManager.removeBinding( entityId.managedObjectName )
      dataManager.removeObject( managedObject )
    } catch {
      case e : NameNotBoundException => // Ignore if no such object exists
    }

  }

}