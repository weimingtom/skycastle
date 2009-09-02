package org.skycastle.client.rendering

import _root_.org.skycastle.entity.{Entity, EntityId}
import com.jme.system.DisplaySystem
import com.jme.scene.Spatial

/**
 * Somethign that is shown with a 3D shape in the 3D renderer.
 * Normally contains player perception of some server side object.
 */
@serializable
@SerialVersionUID(1)
abstract class VisibleEntity extends Entity {


  @transient
  private var spatial : Spatial = null

  private var currentSpace : EntityId = null

  def space : EntityId = currentSpace
  def setSpace( space : EntityId) { currentSpace = space }

  def getSpaceEntity : SpaceEntity = container.getEntityForUpdate( space ) match {
    case Some(x :SpaceEntity ) => x
    case _ => null
  }

  final def getSpatialIfExists : Spatial = spatial

  /**
   * The 3D object for this appearance.
   */
  final def getSpatial(display : DisplaySystem) : Spatial = {
    if (spatial == null) spatial = createSpatial( display )

    spatial
  }

  protected def createSpatial(display : DisplaySystem) : Spatial

  


}