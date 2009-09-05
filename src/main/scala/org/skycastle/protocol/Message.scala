package org.skycastle.protocol


import entity.EntityId
import util.Parameters

/**
 * The messages passed between the server and clients.
 *
 * Consists of an action call on a specified entity.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
case class Message( calledEntity : EntityId, calledAction : Symbol, parameters : Parameters )