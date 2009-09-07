package org.skycastle.entity

/**
 * Something that listens to changes to a property, and calls a listener if the trigger triggers.
 * 
 * @author Hans Haggstrom
 */

trait Trigger {

  def init( value : Any, triggerListener : => Unit, timestamp : Long )

  def valueChanged( newValue : Any, timestamp : Long )

}