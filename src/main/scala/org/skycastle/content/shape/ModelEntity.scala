package org.skycastle.content.shape

import com.jme.scene.Spatial
import composite.CompositeEntity

/**
 * Represents a 3D Model that can consist of various parts.
 */
@serializable
@SerialVersionUID(1)
class ModelEntity extends CompositeEntity {
  type VIEW = Spatial
  type COMPONENT = ModelPart


}