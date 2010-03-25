package org.skycastle.server


import _root_.org.skycastle.entity.entitycontainer.EntityContainer
import com.sun.sgs.app.{NameNotBoundException, DataManager, ManagedObject, AppContext}
import org.skycastle.entity.entitycontainer.darkstar.DarkstarEntityContainer
import org.skycastle.entity.{EntityLogger, EntityId, Entity}
import org.skycastle.network.Message
import org.skycastle.util.Parameters

/**
 * ManagedObject wrapper for an Entity.
 * Also provides the EntityContainer services for the Entity.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class ManagedEntity[T <: Entity]( entity : T ) extends ManagedObject with EntityContainer {

  // Forward the requests to the DarkstarEntityContainer singleton

  def storeEntity(entity: Entity, initParams : Parameters) : EntityId  = DarkstarEntityContainer.storeEntity(entity, initParams)
  def markForUpdate( entity : Entity )                        = DarkstarEntityContainer.markForUpdate(entity)
  def getEntity(entityId: EntityId) : Option[Entity]          = DarkstarEntityContainer.getEntity( entityId )
  def getEntityForUpdate(entityId: EntityId) : Option[Entity] = DarkstarEntityContainer.getEntityForUpdate(entityId)
  def removeEntity(entityId: EntityId)                        = DarkstarEntityContainer.removeEntity(entityId)

  def bindName(name: String, entity: Entity)                  = DarkstarEntityContainer.bindName( name, entity )
  def getNamedEntity(name: String)                            = DarkstarEntityContainer.getNamedEntity( name )
  def getNamedEntityForUpdate(name: String)                   = DarkstarEntityContainer.getNamedEntityForUpdate( name )
  def removeBinding(name: String)                             = DarkstarEntityContainer.removeBinding( name )
  
  def call(message : Message)                                       = DarkstarEntityContainer.call(message)
  def call(message: Message, delay_ms: Long, repeatDelay_ms: Long)  = DarkstarEntityContainer.call(message, delay_ms, repeatDelay_ms);
  def call(message: Message, delay_ms: Long)                        = DarkstarEntityContainer.call(message, delay_ms)
}

