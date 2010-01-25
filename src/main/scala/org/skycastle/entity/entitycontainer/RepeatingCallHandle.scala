package org.skycastle.entity.entitycontainer

/**
 * A handle that can be used to stop a repeating call.
 * 
 * @author Hans Haggstrom
 */
trait RepeatingCallHandle {
  def stop()
}

