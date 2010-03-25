package org.skycastle.entity.entitycontainer.darkstar

import org.skycastle.entity.entitycontainer.RepeatingCallHandle
import com.sun.sgs.app.PeriodicTaskHandle

class DarkstarRepeatingCallHandle(handle: PeriodicTaskHandle) extends RepeatingCallHandle {
  def stop() = {
    handle.cancel()
  }
}
