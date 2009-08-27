package org.skycastle.entity

/**
 * A sequence of action invocations to run on an entity when it is created.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class EntityInitializationScript( actions : List[ActionCall] ) {
  def run( entity : Entity) {
    actions foreach { a : ActionCall =>
      entity.invoke(  a.actionId, a.parameters )
    }
  }
}

