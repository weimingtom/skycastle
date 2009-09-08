package org.skycastle.entity

/**
 * Something that listens to changes to a property, and can cause a call to a listener if the trigger triggers.
 * 
 * @author Hans Haggstrom
 */

trait Trigger {

  def init( value : Any, triggerListener : => Unit, timestamp : Long )

  /**
   * Return true if the listener should be notified, false if not.
   */
  def valueChanged( newValue : Any, timestamp : Long ) : Boolean

}