package org.skycastle.entity


import util.Parameters

/**
 * Action invocation, without the target entity id.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class ActionCall( actionId : String, parameters : Parameters )
