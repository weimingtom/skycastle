package org.skycastle.content.geometry


import entity.Entity

/**
 * A 2D area that has some height and base level, creating a three dimensional volume.
 *
 * Used for parts of buildings etc.
 *
 * May have details attached to the outside surface (windows, doors, portals, exterior details, etc).
 *
 * Will block movement normally.
 * 
 * @author Hans Haggstrom
 */
case class RaisedArea extends GeoEntity {

  var floorZ : Double = 0
  var height : Double = 2

  def create3dView() {

    if (geometry == null || geometry.getDimension < 2 )
    {
      // TODO: Return empty 3D representation
    }

    val edges = geometry.getBoundary
    var n = edges.getNumGeometries

    while ( n > 0) {
      n -= 1

      val geo = edges.getGeometryN( n )
      if (geo.getDimension >= 2 ) {
        // TODO: Get the exterior ring.  Maybe even take into account internal holes?

        // Generate 3D shape using the outline
        // TODO
      }

    }

  }

}


