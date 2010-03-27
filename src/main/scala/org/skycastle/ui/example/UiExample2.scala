package org.skycastle.ui.example


import org.skycastle.entity.script.{ActionCall, ActionSequenceScript}
import org.skycastle.entity.EntityId
import org.skycastle.util.{Parameters, SimpleFrame}
import org.skycastle.ui.ScreenEntity

/**
 * 
 * 
 * @author Hans Haggstrom
 */

object UiExample2 {
  def main(args: Array[String]) {

    
    val initializationScript = ActionSequenceScript( List(
      ActionCall( 'addComponent, Parameters(
      'componentType -> "panel",
      'id -> 'root,
      'parent -> null,
      'layout -> ""
      ) ) ,
      ActionCall( 'addComponent, Parameters(
      'componentType -> "label",
      'id -> 'testLabel1,
      'parent -> 'root,
      'text -> "Test label 1",
      'tooltip -> "A test label"
      ) ),
      ActionCall( 'addComponent, Parameters(
      'componentType -> "label",
      'id -> 'testLabel2,
      'parent -> 'root,
      'text -> "Test label 2",
      'tooltip -> "A test label"
      ) ),
      ActionCall( 'addComponent, Parameters(
      'componentType -> "button",
      'id -> 'testbutton_1,
      'parent -> 'root,
      'text -> "Press Me",
      'tooltip -> "A test button",
      'invokedEntity -> EntityId("entity432455"),
      'invokedMethod -> "testAction"
      ) ),
      ActionCall( 'removeComponent, Parameters(
      'id -> 'testLabel1
      ) ) )
      )
    

    val screen = new ScreenEntity
    initializationScript.run( screen, Parameters() )

    val view = screen.getView

    new SimpleFrame( "UiExample", view)

  }
}

