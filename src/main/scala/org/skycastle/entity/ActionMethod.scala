package org.skycastle.entity

import _root_.org.skycastle.util.Parameters
import java.lang.reflect.Method

object ActionMethodConstants {
  val CALLER_ID = Symbol("$callerId")
  val PARAMETERS = Symbol("$parameters")
}

/**
 * Utility class for holding discovered action methods in an Entity.
 */
case class ActionMethod( host : Entity, method : Method, parameterMapping : List[Symbol] ) {

  def call( caller : EntityId, parameters : Parameters ) {

    val invocationParameters : Array[Object] = parameterMapping.map( { sourceParamName : Symbol =>
      // Handle special parameter mappings:
      if ( sourceParamName == ActionMethodConstants.CALLER_ID ) caller
      else if ( sourceParamName == ActionMethodConstants.PARAMETERS ) parameters
      else parameters.getOrElse[Object]( sourceParamName, null.asInstanceOf[Object] )
    }).toArray

    method.invoke( host, invocationParameters : _* )
  }

}