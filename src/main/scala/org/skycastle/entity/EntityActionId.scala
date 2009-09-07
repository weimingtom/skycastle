package org.skycastle.entity

/**
 * The ID of a specific action in a specific entity.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
case class EntityActionId( entity : EntityId, action : Symbol ) 