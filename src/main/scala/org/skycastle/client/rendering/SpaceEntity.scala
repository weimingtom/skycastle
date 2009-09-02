package org.skycastle.client.rendering

import _root_.org.skycastle.entity.{Entity, EntityId}
import com.jme.scene.Node
import com.jme.system.DisplaySystem
import com.jme.math.Vector3f

/**
 * Contains a 3D environment.
 *
 * Supplies boundaries, environmental context, and physical simulation parameters.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class SpaceEntity  extends Entity {

  private var entities: List[EntityId] = Nil

  private var hostSpace : EntityId = null

  @transient
  private var scene: Node = null
  @transient
  private var display: DisplaySystem = null


  final def getHostSpace = hostSpace
  final def setHostSpace ( space : EntityId ) { hostSpace = space}

  /**
   * Called during the render phase.
   */
  def render() {}

  /**
   *  The entities currently in the space
   */
  final def getEntities: List[EntityId] = entities

  final def addEntity(entity: EntityId) : Unit = addEntity( entity, new Vector3f() )



  final def addEntity(entity: EntityId, position: Vector3f) {
 /*TODO

    require(entity != null && !entities.contains(entity))


    if (entity.space != null) {
      entity.space.removeEntity( entity )
    }

    entities = entity :: entities

    entity.setSpace( this )
    entity.setPosition( position )

    onEntityAdded(entity, position)
 */ }

  final def removeEntity(entity: EntityId) {
 /*TODO

    require(entity != null)

    if (entities.contains(entity)) {
      entities = entities.remove(_ == entity)

      entity.setSpace( null )

      onEntityRemoved(entity)
    }
 */ }

  protected def onEntityAdded(entity: EntityId, position: Vector3f) {}

  protected def onEntityRemoved(entity: EntityId) {}

  final def getSceneIfExists: Node = scene


  /**
   * The 3D scene for this space.
   */
  final def getScene(display: DisplaySystem): Node = {
    require(display != null)

    if (scene == null) {
      this.display = display
      scene = createScene(display)
    }

    scene
  }

  protected def createScene(display: DisplaySystem): Node = {
  null
  }

  protected final def getDisplay = display

}