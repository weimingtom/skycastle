package org.skycastle.rendering

import _root_.org.skycastle.entity.Entity
import com.jme.math.Vector3f
import com.jme.system.DisplaySystem

/**
 * Describes a position in 3D space and camera parameters.
 * 
 */
class CameraWrapper(display: DisplaySystem, width: Int, height: Int) {

  private val loc  = new Vector3f( 0, 0, 0 )
  private val left = new Vector3f( -0.5f, 0, 0.5f )
  private val up   = new Vector3f( 0, 1, 0 )
  private val dir  = new Vector3f( -0.5f, 0, -0.5f )

  //initialize the camera
  private val cam = display.getRenderer().createCamera(width, height)

  def camera = cam

  cam.setFrustumPerspective(45.0f, width.toFloat / height.toFloat, 1, 1000)

  // Move our camera to a correct place and orientation.
  cam.setFrame(loc, left, up, dir)

  /**Signal that we've changed our camera's location/frustum. */
  cam.update()

  display.getRenderer().setCamera(cam)

  def setPosition(position: Vector3f) : Unit = setPosition(position.x, position.y, position.z)

  def setPosition(x: Float, y: Float, z: Float) {
    loc.setX(x)
    loc.setY(y)
    loc.setZ(z)

    cam.update()
  }
}