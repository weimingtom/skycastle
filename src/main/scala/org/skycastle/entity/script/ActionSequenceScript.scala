package org.skycastle.entity.script


import util.Parameters

/**
 * A script that just consists of a sequence of actions to apply to the target.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
final case class ActionSequenceScript( actions : List[ActionCall] ) extends Script {

  def run(hostEntity: Entity, parameters: Parameters) {
    if (hostEntity != null && actions != null ) {
      actions foreach { _.call(hostEntity) }
    }
  }

}

