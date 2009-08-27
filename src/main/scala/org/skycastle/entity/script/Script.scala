package org.skycastle.entity.script


import util.Parameters

/**
 * Contains a sequence of actions calls that can be invoked on some target entity.
 *
 * May take parameters, and calculate what actions to take based on the parameters.
 *
 * May even query the entity it is applied on, and any other entities in the container?
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
trait Script {

  def run( hostEntity : Entity, parameters : Parameters )

}

