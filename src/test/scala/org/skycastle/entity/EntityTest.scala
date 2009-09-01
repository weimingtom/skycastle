package org.skycastle.entity


import _root_.junit.framework.TestCase
import entitycontainer.SimpleEntityContainer

import org.junit._
import Assert._

/**
 * 
 * 
 * @author Hans Haggstrom
 */
@Test
class EntityTest {

  @Test
  def testEntityGetsIdWhenAddedToContainer {
    val entity = new Entity()
    val entityContainer = new SimpleEntityContainer()

    assertNull( entity.id )
    entityContainer.storeEntity( entity )
    assertNotNull( entity.id )
    assertEquals( entityContainer,  entity.container  )
  }


}