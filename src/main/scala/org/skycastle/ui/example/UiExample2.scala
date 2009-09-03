package org.skycastle.ui.example


import entity.script.{ActionCall, ActionSequenceScript}
import entity.EntityId
import util.{Parameters, SimpleFrame}
/**
 * 
 * 
 * @author Hans Haggstrom
 */

object UiExample2 {
  def main(args: Array[String]) {

    val initializationScript = ActionSequenceScript( List(
      ActionCall( "addComponent", Parameters( Map(
      'componentType -> "panel",
      'id -> 'root,
      'parent -> null,
      'layout -> ""
      ) ) ),
      ActionCall( "addComponent", Parameters( Map(
      'componentType -> "label",
      'id -> 'testLabel1,
      'parent -> 'root,
      'text -> "Test label 1",
      'tooltip -> "A test label"
      ) ) ),
      ActionCall( "addComponent", Parameters( Map(
      'componentType -> "label",
      'id -> 'testLabel2,
      'parent -> 'root,
      'text -> "Test label 2",
      'tooltip -> "A test label"
      ) ) ),
      ActionCall( "addComponent", Parameters( Map(
      'componentType -> "button",
      'id -> 'testbutton_1,
      'parent -> 'root,
      'text -> "Press Me",
      'tooltip -> "A test button",
      'invokedEntity -> EntityId("entity-432455"),
      'invokedMethod -> "testAction"
      ) ) ),
      ActionCall( "removeComponent", Parameters( Map(
      'id -> 'testLabel1
      ) ) )
      ) )
    

    val screen = new ScreenEntity
    initializationScript.run( screen, new Parameters() )

    val view = screen.getView

    new SimpleFrame( "UiExample", view)

  }
}

