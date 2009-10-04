package org.skycastle.entity

/**
 * The ID of a specific action in a specific entity.
 *
 * @deprecated This seems unnecessary, we can use Message instead in some cases, or just list the parameters.
 *
 * @author Hans Haggstrom
 */
@deprecated
@serializable
@SerialVersionUID(1)
case class EntityActionId( entity : EntityId, action : Symbol ) 