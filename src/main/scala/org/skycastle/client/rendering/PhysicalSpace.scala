package org.skycastle.client.rendering

import _root_.org.skycastle.entity.{EntityId, Entity}
import com.jme.math.Vector3f
import com.jme.system.DisplaySystem

/**
 *
 *
 * @author Hans Haggstrom
 */

abstract class PhysicalSpace extends SpaceEntity  {

  private val tempForce    = new Vector3f()
  private val tempPos      = new Vector3f()
  private val tempVelocity = new Vector3f()

  var airResistance : Float = 0.1f

  /**
   * Updates physics and other things that need updating.
   */
  def update( timeSinceLastUpdate_s : Float ) {

    getEntities foreach ((entity : EntityId) => {

/* TODO
      entity.update( timeSinceLastUpdate_s )

      // Apply forces inside the space on the entities
      tempForce.zero
      calculateForce( entity, tempForce )

      entity.simulatePhysics( timeSinceLastUpdate_s, tempForce, airResistance )

      // Check collisions with space boundaries, and adjust positions
      entity.getPosition( tempPos )
      entity.getVelocity( tempVelocity )

      checkCollision( entity, tempPos, tempVelocity )
*/
    })

  }

  /**
   * Check if the entity has collided with the outer boundaries of the space, or a portal opening, or other
   * physical surface, and in that case move the entity to the correct position, notify it of the collision,
   * and in case of portals, move to the correct space.
   */
  protected def checkCollision( entity : EntityId, position : Vector3f, velocity : Vector3f )

  /**
   * Calculates the force to apply to the specified entity in this space
   */
  protected def calculateForce( entity : EntityId, forceOut : Vector3f )


  override protected def onEntityAdded(entity: EntityId, position: Vector3f) = {

    val scene = getSceneIfExists
    if (scene != null) {
/* TODO
      scene.attachChild( entity.appearance.getSpatial( getDisplay ) )
*/
    }

  }

  override protected def onEntityRemoved(entity: EntityId) = {

    val scene = getSceneIfExists
    if (scene != null) {
/* TODO
      scene.detachChild( entity.appearance.getSpatial( getDisplay ) )
*/
    }

  }


}