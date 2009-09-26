package org.skycastle.entity.script


import network.Message
import util.Parameters

/**
 * Action invocation, without the target entity id.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class ActionCall( actionId : Symbol, parameters : Parameters ) {

  def call( hostEntity : Entity ) {
    hostEntity.call( hostEntity.id, actionId, parameters )
  }

}
