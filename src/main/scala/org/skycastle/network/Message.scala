package org.skycastle.network


import entity.EntityId
import util.Parameters

/**
 * The messages passed between the server and clients.
 *
 * Consists of an action call on a specified entity.
 *
 * The parameters contains the data to be passed to the receiving action.
 * The network system can handle transfer of at least the basic primitive types, (scala) lists, sets, and maps,
 * as well as EntityID:s, Symbols, Strings, and Parameters.  The coder can also define own transferable types
 * by implementing the Transferable interface, which should convert an object to and from some composition
 * of the types listed above. 
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
case class Message( callingEntity : EntityId, calledEntity : EntityId, calledAction : Symbol, parameters : Parameters )