package org.skycastle.ui.example


import org.skycastle.entity.EntityId
import org.skycastle.util.{SimpleFrame, Parameters}
import org.skycastle.ui.ScreenEntity

/**
 * 
 * 
 * @author Hans Haggstrom
 */

object UiExample {
  def main(args: Array[String]) {

    val screen = new ScreenEntity()

    screen.addComponent( "panel", 'root, null, Parameters( 'layout -> "" ) )
    screen.addComponent( "label", 'testlabel_1, 'root, Parameters( 'text -> "Test Label 1", 'tooltip -> "Example label 1" ) ) 
    screen.addComponent( "label", 'testlabel_2, 'root, Parameters( 'text -> "Test Label 2", 'tooltip -> "Example label 2" ) )
    screen.addComponent( "panel", 'panel2, 'root, Parameters( 'layout -> ""  ) )
    screen.addComponent( "button", 'testButton_1, 'panel2, Parameters( 'text -> "Click Me", 'tooltip -> "Test Button", 'invokedEntity -> EntityId("entity-432455"), 'invokedMethod -> "testAction" ) )

    screen.removeComponent( 'testlabel_1 )
    
    val view = screen.getView

    new SimpleFrame( "UiExample", view)
  }
}

