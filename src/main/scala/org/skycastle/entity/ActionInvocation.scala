package org.skycastle.entity


import util.Parameters

/**
 * Action invocation, without the target entity id.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class ActionInvocation( actionId : String, parameters : Parameters ) 
