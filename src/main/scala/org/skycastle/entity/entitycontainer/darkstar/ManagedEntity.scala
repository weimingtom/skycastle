package org.skycastle.server


import _root_.org.skycastle.entity.entitycontainer.EntityContainer
import com.sun.sgs.app.{NameNotBoundException, DataManager, ManagedObject, AppContext}
import entity.entitycontainer.darkstar.DarkstarEntityContainer
import entity.{EntityLogger, EntityId, Entity}
import util.Parameters
/**
 * ManagedObject wrapper for an Entity.
 * Also provides the EntityContainer services for the Entity.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class ManagedEntity[T <: Entity]( entity : T ) extends ManagedObject with EntityContainer {

  // Fordward the requests to the DarkstarEntityContainer singleton

  def storeEntity(entity: Entity) : EntityId                  = DarkstarEntityContainer.storeEntity(entity)
  def markForUpdate( entity : Entity )                        = DarkstarEntityContainer.markForUpdate(entity)
  def getEntity(entityId: EntityId) : Option[Entity]          = DarkstarEntityContainer.getEntity( entityId )
  def getEntityForUpdate(entityId: EntityId) : Option[Entity] = DarkstarEntityContainer.getEntityForUpdate(entityId)
  def removeEntity(entityId: EntityId)                        = DarkstarEntityContainer.removeEntity(entityId)

  def bindName(name: String, entity: Entity)                  = DarkstarEntityContainer.bindName( name, entity )
  def getNamedEntity(name: String)                            = DarkstarEntityContainer.getNamedEntity( name )
  def getNamedEntityForUpdate(name: String)                   = DarkstarEntityContainer.getNamedEntityForUpdate( name )
  def removeBinding(name: String)                             = DarkstarEntityContainer.removeBinding( name )


  def call(callingEntity: EntityId, calledEntity: EntityId, actionName: String, parameters: Parameters) {
    EntityLogger.logError( "ManagedEntity.call Not yet implemented" )
    // TODO: handle
  }
}

