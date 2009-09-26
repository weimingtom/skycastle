package org.skycastle.entity.entitycontainer.darkstar

import _root_.org.skycastle.server.ManagedEntity
import com.sun.sgs.app.{TaskManager, DataManager, NameNotBoundException, AppContext}
import java.lang.ClassCastException
import network.Message
import util.Parameters
import util.ParameterChecker._

/**
 * A singleton object for accessing the DarkstarEntityContainer
 */
object DarkstarEntityContainer extends EntityContainer {


  def storeEntity(entity: Entity, initializationParameters : Parameters) : EntityId = {
    storeManagedEntity( new ManagedEntity( entity ), initializationParameters )
  }

  def storeManagedEntity( managedEntity : ManagedEntity[_ <: Entity], initializationParameters : Parameters ) : EntityId = {
    val dataManager : DataManager = AppContext.getDataManager

    val name = "entity_" + dataManager.getObjectId( managedEntity ).toString
    val id = EntityId( name )

    managedEntity.entity.setId( id )
    managedEntity.entity.container = managedEntity

    dataManager.setBinding( name, managedEntity )

    managedEntity.entity.initEntity(initializationParameters)

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
      entityId.managedObjectName match {
        case Some(name) => Some[Entity]( AppContext.getDataManager.getBinding( name ).asInstanceOf[ManagedEntity[Entity]].entity )
        case None => None
      }
    } catch {
      case e : NameNotBoundException => None
      case e : ClassCastException => None
    }
  }

  def getEntityForUpdate(entityId: EntityId) : Option[Entity] = {
    if (entityId == null) return None
    else try {
      entityId.managedObjectName match {
        case Some(name) => Some[Entity]( AppContext.getDataManager.getBindingForUpdate( name ).asInstanceOf[ManagedEntity[Entity]].entity )
        case None => None
      }
    } catch {
      case e : NameNotBoundException => None
      case e : ClassCastException => None
    }
  }

  def removeEntity(entityId: EntityId) {
    val dataManager : DataManager = AppContext.getDataManager

    try {
      entityId.managedObjectName match  {
        case Some( name ) => {
          val managedObject = dataManager.getBinding( name )
          dataManager.removeBinding( name )
          dataManager.removeObject( managedObject )
        }
        case None => EntityLogger.logWarning( "Can not remove entity "+entityId+" from container "+this+": The Entity is not hosted in this container." )
      }
    } catch {
      case e : NameNotBoundException => EntityLogger.logWarning( "Can not remove entity "+entityId+" from container "+this+": The Entity was not found.", e )
    }

  }


  def bindName(name: String, entity: Entity) {
    requireNotNull( name, 'name )
    requireNotNull( entity, 'entity )
    if (entity.id == null) throw new IllegalArgumentException( "Can not bind entity '"+entity+"' to name '"+name+"', it is not yet stored in any EntityCOntainer." )

    val dataManager : DataManager = AppContext.getDataManager

    entity.id.managedObjectName match {
      case Some( entityId ) => {
        val managedEntity = AppContext.getDataManager.getBinding( entityId )
        dataManager.setBinding( getBindingName( name ), managedEntity )
      }
      case None => throw new IllegalStateException( "Can not bind a name to the entity "+entity+", because it doesn't have a one-path id, meaning it is hosted by another entity container." )
    }
  }

  def getNamedEntity(name: String) : Option[Entity] = {
    if (name == null) return None
    else try {
      Some[Entity]( AppContext.getDataManager.getBinding( getBindingName( name ) ).asInstanceOf[ManagedEntity[Entity]].entity )
    } catch {
      case e : NameNotBoundException => None
      case e : ClassCastException => None
    }
  }

  def getNamedEntityForUpdate(name: String) : Option[Entity] = {
    if (name == null) return None
    else try {
      Some[Entity]( AppContext.getDataManager.getBindingForUpdate( getBindingName( name ) ).asInstanceOf[ManagedEntity[Entity]].entity )
    } catch {
      case e : NameNotBoundException => None
      case e : ClassCastException => None
    }
  }

  def removeBinding(name: String)  {
    val dataManager : DataManager = AppContext.getDataManager

    try {
      dataManager.removeBinding( getBindingName( name ))
    } catch {
      case e : NameNotBoundException =>  EntityLogger.logWarning( "Can not remove name binding '"+name+"' from container "+this+": Nothing was bound to the name.", e )
    }
  }

  private def getBindingName( name : String ) = "namedEntity_" + name


  def call(message : Message) {

    val taskManager : TaskManager = AppContext.getTaskManager

    taskManager.scheduleTask( ActionCallTask( message ) )
  }
}



