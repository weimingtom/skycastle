package org.skycastle.entity


import com.sun.sgs.app.ManagedObject
import java.io.Serializable
import util.{Properties, Description}
/**
 * Specifies a basic entity type to start from, and the operations to do to it to get to the final entity.
 * May take parameters that are used when applying the operations.
 *
 * Could also provide default values for properties, to avoid too much duplication of data?
 *
 * Immutable, creates a new copy when updated.
 * Or maybe an archetype should be mutable, and contain its update history, so it can generate the desired version?
 *
 * TODO: Add owner & update etc rights
 *


Types of objects


---
Archetypes: - these can create entities
machine drawing - instantiable
house plan - instantiable
composite item creation parameters - instantiable
spell description - instantiable
world map (generative) - instantiable
entity archetype - instantiable

game - instantiable

Information objects: - these can create documents - could still be represented as entities, but usually only instantiated on client side.
ui layout - instantiable?
text (could also be created generatively by the archetype?)
picture
texture
3d shape
music
sound sample
script / program
brush for painting
---
information based
sometimes used to create entities
collaborative editing possible
single instance of each specific work
version history
one current revision
possible to request older revisions and rollback changes
export / import to other games / generic repositories
access rights for editing / reading
immutable, new revision created after changes
old revisions not kept in memory always, instead only sequence of actions done (version history), recreate the version when needed, possibly keep cached.
Not always possible to create some entity instance? instead the archetype is the raw data, can be embedded / displayed in various places though (text, picure, sound, music, 3d shape, etc)


---
entity instance - server side
ui instance - client side
spell instance  - server side
build goal/site (when blueprins are instantiated on some location they create build goals/plans/building sites, that can then be built bit by bit)
character instance - server side
ai instance - server side
terrain instance - separate entities for client and server?
entity perception/proxy (what can be seen / perceived of an entity - client side / ai model representation).  Can also forward action requests to a controlled object?
---
created based on an archetype
may be modified
may simulate state
not user editable directly, only through simulation
no revision control
but updates sent to clients from server


 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
abstract case class Archetype( id : ArchetypeId, parameters : Properties )  {

  val defaultProperties : Properties = new Properties()

  def createEntity( parameters : Properties ) : Entity = null

  def getDefaultProperty( id : Symbol, default : Serializable) : Serializable = defaultProperties.get( id, default )

  /**
   * Create a new revision of this Archetype, a copy of this one with the specified update applied.
   */
  def update( updateType : String, parameters : Properties ) : Archetype

  /**
   * Create a new Archetype that is a branch / copy of this one.
   */
  def branch( newId : String, description : Description ) : Archetype
  
}


