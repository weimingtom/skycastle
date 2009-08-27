package org.skycastle.ui.example


import entity.{ActionSequenceScript, ActionCall, EntityInitializationScript, EntityId}
import util.{Parameters, SimpleFrame}
/**
 * 
 * 
 * @author Hans Haggstrom
 */

object UiExample2 {
  def main(args: Array[String]) {

    val initializationScript = ActionSequenceScript( List(
      ActionCall( "addUiComponent", Parameters( Map(
      'componentType -> 'panel,
      'id -> 'root,
      'parent -> null,
      'layout -> ""
      ) ) ),
      ActionCall( "addUiComponent", Parameters( Map(
      'componentType -> 'label,
      'id -> 'testLabel1,
      'parent -> 'root,
      'text -> "Test label 1",
      'tooltip -> "A test label"
      ) ) ),
      ActionCall( "addUiComponent", Parameters( Map(
      'componentType -> 'label,
      'id -> 'testLabel2,
      'parent -> 'root,
      'text -> "Test label 2",
      'tooltip -> "A test label"
      ) ) ),
      ActionCall( "addUiComponent", Parameters( Map(
      'componentType -> 'button,
      'id -> 'testbutton_1,
      'parent -> 'root,
      'text -> "Press Me",
      'tooltip -> "A test button",
      'invokedEntity -> EntityId("entity-432455"),
      'invokedMethod -> "testAction"
      ) ) ),
      ActionCall( "removeUiComponent", Parameters( Map(
      'id -> 'testLabel1
      ) ) )
      ) )
    

    val archetype = new ScreenArchetype( Parameters( Map( 'entityInitializationScript -> initializationScript ) ) )

    val screen = archetype.createEntity( new Parameters() )

    val view = screen.getView

    new SimpleFrame( "UiExample", view)

  }
}

