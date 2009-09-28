package org.skycastle.content.ui

/**
 * Shows a table with data from some source.
 *
 * The data items should implement the TypedGetters trait (that is Parameters or Entities will both work).
 * EntityId:s will normally be looked up and treated as Entities.
 * Other types of data is treated as a Property with a single value field of String type.
 *
 * For each column, it is specified which property should be shown, and how it should be visualized.
 *
 * The data can be sorted by a sequence of columns, or in raw order.
 *
 * The backend is queried for the data rows that fit on screen, in the specified sort order.
 * 
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID( 1 )
class TableUi extends UiEntity {

  // TODO: Source specification property

  // TODO: Column headers, default sizes & stretches, and data visualizer for each column


  // TODO: creator and getter for swing ui

  // TODO: creator and getter for 3D ui
  
}

