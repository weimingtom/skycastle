package org.skycastle.ui.example


import entity.EntityId
import javax.swing.JFrame
import util.{SimpleFrame, Parameters}
/**
 * 
 * 
 * @author Hans Haggstrom
 */

object UiExample {
  def main(args: Array[String]) {

    val screen = new ScreenEntity(null)

    screen.addUiComponent( 'panel, 'root, null, Parameters( Map( 'layout -> "" ) ) )
    screen.addUiComponent( 'label, 'testlabel_1, 'root, Parameters( Map( 'text -> "Test Label 1", 'tooltip -> "Example label 1" ) ) )
    screen.addUiComponent( 'label, 'testlabel_2, 'root, Parameters( Map( 'text -> "Test Label 2", 'tooltip -> "Example label 2" ) ) )
    screen.addUiComponent( 'button, 'testButton_1, 'root, Parameters( Map( 'text -> "Click Me", 'tooltip -> "Test Button", 'invokedEntity -> EntityId("entity-432455"), 'invokedMethod -> "testAction" ) ) )

    val view = screen.getView

    new SimpleFrame( "UiExample", view)
  }
}

