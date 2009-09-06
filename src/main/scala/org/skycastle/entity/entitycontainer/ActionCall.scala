package org.skycastle.entity.entitycontainer


import util.Parameters

/**
 * Helper class to hold the details of a call to an action.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
case class ActionCall( callingEntity : EntityId, calledEntity : EntityId, actionName : Symbol, parameters : Parameters )