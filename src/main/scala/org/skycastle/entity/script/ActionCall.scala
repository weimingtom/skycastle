package org.skycastle.entity.script


import util.Parameters

/**
 * Action invocation, without the target entity id.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class ActionCall( actionId : String, parameters : Parameters ) {

  def call( hostEntity : Entity ) {
    hostEntity.call( hostEntity.id, actionId, parameters )
  }

}