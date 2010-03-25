package org.skycastle.entity.entitycontainer.simple

import org.skycastle.entity.entitycontainer.{SimpleEntityContainer, RepeatingCallHandle}

class SimpleRepeatingCallHandle(container: SimpleEntityContainer, callHandle: Long) extends RepeatingCallHandle {
  def stop() = container.stopTask(callHandle)
}
