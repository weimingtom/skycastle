package org.skycastle.content.model

import com.jme.scene.Spatial
import composite.CompositeEntity

/**
 * Represents a 3D Model that can consist of various parts.
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ModelEntity extends CompositeEntity {
  type VIEW = Spatial
  type COMPONENT = ModelPart

  val componentClass = classOf[ModelPart]
}