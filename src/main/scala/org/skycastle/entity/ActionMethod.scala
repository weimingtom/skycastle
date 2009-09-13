package org.skycastle.entity

import _root_.org.skycastle.util.Parameters
import java.lang.reflect.Method

/**
 * Utility class for holding discovered action methods in an Entity.
 */
case class ActionMethod( host : Entity, method : Method ) {

  def call( caller : EntityId, parameters : Parameters ) {

  }

}