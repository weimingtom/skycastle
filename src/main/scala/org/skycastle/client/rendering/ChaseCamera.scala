package org.skycastle.client.rendering


import com.jme.input.ChaseCamera
import com.jme.input.thirdperson.ThirdPersonMouseLook
import com.jme.math.{Vector3f, FastMath}
import com.jme.renderer.Camera
import com.jme.scene.Spatial
import java.util.HashMap
/**
 *
 *
 * @author Hans Haggstrom
 */
// TODO: Tune parameters, or rewrite to be independent of movement speed.
class CharacterChaseCamera(target: Spatial, cameraHeight : Float,  camera : Camera) {

  val chaseCamera : ChaseCamera = buildChaseCamera()

  def update( interpolation : Float ) = chaseCamera.update( interpolation )

  def buildChaseCamera() : ChaseCamera = {
    val targetOffset = new Vector3f()
    targetOffset.y = cameraHeight
    val props = new HashMap[String,Object]()
    props.put(ThirdPersonMouseLook.PROP_MAXROLLOUT, "100")
    props.put(ThirdPersonMouseLook.PROP_MINROLLOUT, "2")
    props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset)
    props.put(ThirdPersonMouseLook.PROP_MAXASCENT, "" + 80 * FastMath.DEG_TO_RAD)
    props.put(ThirdPersonMouseLook.PROP_MINASCENT, "" + -80 * FastMath.DEG_TO_RAD)
    props.put(ThirdPersonMouseLook.PROP_INVERTEDY, boolean2Boolean(true))
    props.put(ThirdPersonMouseLook.PROP_MOUSEYMULT, "" + 20 )
    props.put(ChaseCamera.PROP_INITIALSPHERECOORDS, new Vector3f(5, 0, 30 * FastMath.DEG_TO_RAD))
    props.put(ChaseCamera.PROP_TARGETOFFSET, targetOffset)
    props.put(ChaseCamera.PROP_ENABLESPRING, boolean2Boolean(true))


    val chaser = new ChaseCamera(camera, target, props)
    chaser.setMaxDistance(100)
    chaser.setMinDistance(2)
    chaser
  }


}