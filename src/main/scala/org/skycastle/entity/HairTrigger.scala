package org.skycastle.entity
/**
 * A trigger that triggers at every change.
 */

class HairTrigger extends Trigger {

  def init(value: Any, triggerListener : => Unit, timestamp: Long) {}

  def valueChanged(newValue: Any, timestamp: Long) : Boolean = true
}