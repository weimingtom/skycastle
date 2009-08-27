package org.skycastle.entity

/**
 * A sequence of action invocations to run on an entity when it is created.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
case class EntityInitializationScript( actions : List[ActionInvocation] ) {
  def run( entity : Entity) {
    actions foreach { a : ActionInvocation =>
      entity.invoke( a.actionId, a.parameters )
    }
  }
}

