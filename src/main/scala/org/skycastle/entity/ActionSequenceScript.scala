package org.skycastle.entity


import util.Parameters

/**
 * A script that just consists of a sequence of actions to apply to the target.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class ActionSequenceScript( actions : List[ActionCall] ) extends Script {
  def apply(targetEntity: Entity, parameters: Parameters) {
    if (targetEntity != null && actions != null ) {
      actions foreach { action : ActionCall =>
        targetEntity.invoke( action.actionId, action.parameters )
      }
    }
  }
}

