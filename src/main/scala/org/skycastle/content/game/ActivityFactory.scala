package org.skycastle.content.game

import _root_.org.skycastle.entity.Entity
import _root_.org.skycastle.util.{Properties, Parameters}
/**
 * 
 */

class ActivityFactory extends Entity {


  /**
   * Return calls with a description of the activities created by this factory.
   */
  def getDescription( caller : Entity ) {}

  /**
   * Description of the activities created by this factory.
   */
  def description : Parameters = {null}

  def createActivity( parameters : Parameters ) {}

  

}