package org.skycastle.entity


import util.Parameters

/**
 * Some action available on an Entity.
 *
 * Can be forwarded to a local method call, or can be implemented as a Script.
 * 
 * @author Hans Haggstrom
 */
abstract case class Action(description : String) {

  def call( entity : Entity, parameters : Parameters )
}

case class MethodCallAction( description : String, methodName : String ) extends Action(description) {
  def call(entity: Entity, parameters: Parameters) {
    // Get method with specified name on entity, if found.
    // TODO

    // Call the method, pass the Parameters object as a parameter.
    // TODO

    println( "TODO: Implement calling " +methodName+" method on " +entity+ " with parameters " +parameters )
  }
}

case class ScriptCallAction( description : String, script : Script ) extends Action(description) {
  def call(entity: Entity, parameters: Parameters) {
    script.apply( entity, parameters )
  }
}


