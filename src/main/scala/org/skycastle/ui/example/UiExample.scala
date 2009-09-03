package org.skycastle.ui.example


import entity.EntityId
import util.{SimpleFrame, Parameters}
/**
 * 
 * 
 * @author Hans Haggstrom
 */

object UiExample {
  def main(args: Array[String]) {

    val screen = new ScreenEntity()

    screen.addComponent( "panel", 'root, null, Parameters( Map( 'layout -> "" ) ) )
    screen.addComponent( "label", 'testlabel_1, 'root, Parameters( Map( 'text -> "Test Label 1", 'tooltip -> "Example label 1" ) ) )
    screen.addComponent( "label", 'testlabel_2, 'root, Parameters( Map( 'text -> "Test Label 2", 'tooltip -> "Example label 2" ) ) )
    screen.addComponent( "panel", 'panel2, 'root, Parameters( Map( 'layout -> "" ) ) )
    screen.addComponent( "button", 'testButton_1, 'panel2, Parameters( Map( 'text -> "Click Me", 'tooltip -> "Test Button", 'invokedEntity -> EntityId("entity-432455"), 'invokedMethod -> "testAction" ) ) )

    screen.removeComponent( 'testlabel_1 )
    
    val view = screen.getView

    new SimpleFrame( "UiExample", view)
  }
}

