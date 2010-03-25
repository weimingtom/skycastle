package org.skycastle.entity.entitycontainer


import org.skycastle.network.Message
import org.skycastle.util.Parameters
import org.skycastle.util.ParameterChecker._
import org.skycastle.entity.{EntityLogger, Entity, EntityId}
import collection.mutable.{PriorityQueue, HashMap}
import simple.SimpleRepeatingCallHandle

private final case class ScheduledMessage(time: Long, message: Message, repeatDelay: Long, handle: Long) extends Ordered[ScheduledMessage] {
  // Ensure top of the queue will have item with smallest time.
  override def compare(that: ScheduledMessage) = if (time < that.time) 1 else if (time > that.time) -1 else 0

  def repeating: Boolean = repeatDelay > 0
  def next: Option[ScheduledMessage] = if (repeating) Some(ScheduledMessage(time + repeatDelay, message, repeatDelay, handle)) else None
}

/**
 * A straightforward single-threaded, non-persistent EntityContainer.
 * 
 * @author Hans Haggstrom
 */
class SimpleEntityContainer extends EntityContainer {

  private val entities = new HashMap[EntityId, Entity]()
  private val namedEntities = new HashMap[String, EntityId]()
  private var nextFreeId : Long = 1L
  private var queuedActionCalls : List[Message] = Nil
  private val scheduledActionCalls : PriorityQueue[ScheduledMessage] = new PriorityQueue[ScheduledMessage]();
  private var nextFreeTaskHandle : Long = 1L
  private var currentTime : Long = 0L

  private def nextId() : Long = {
    val id = nextFreeId
    nextFreeId += 1
    id
  }

  def storeEntity(entity: Entity, initializationParameters : Parameters) : EntityId = {

    if (entity.id != null) throw new IllegalStateException( "Can not store entity, it already has an id.  Entity = " + entity )
    
    val id = EntityId( "entity_" + nextId() )

    entity.setId( id )
    entity.container = this
    entities.put( id, entity )

    entity.initEntity( initializationParameters )

    id
  }

  def markForUpdate(entity: Entity) {}

  def getEntity(entityId: EntityId) : Option[Entity] = {
    if (entityId == null) None
    else entities.get( entityId )
  }

  def removeEntity(entityId: EntityId) {
    entities.get( entityId ) match {
      case (entity : Entity) =>
        entity.deinitEntity()
        if (entityId != null) entities.removeKey( entityId )
      case None => EntityLogger.logWarning( "Attempt to remove non-existing entity '"+entityId+"', ignoring." )
      case x =>
        EntityLogger.logError( "Attempt to remove entity which was not an entity '"+x+"'.  Removing it." )
        entities.removeKey( entityId )
    }
  }

  def getEntityForUpdate(entityId: EntityId) = {
    getEntity(entityId)
  }


  def bindName(name: String, entity: Entity) {
    requireNotNull( entity, 'entity )
    requireNotNull( name, 'name )
    if (entity.id == null) throw new IllegalArgumentException("Entity should already be added to an EntityContainer.")

    namedEntities.put( name, entity.id )
  }

  def getNamedEntity(name: String) : Option[Entity] = getEntity( namedEntities.getOrElse( name, null ) )

  def getNamedEntityForUpdate(name: String) : Option[Entity] = getEntityForUpdate( namedEntities.getOrElse( name, null ) )

  def removeBinding(name: String) = {
    if (name != null) namedEntities.removeKey( name )
  }


  def call(message : Message)  {
    queuedActionCalls = queuedActionCalls ::: List( message )
  }

  def call(message: Message, delay_ms: Long) {
    if (delay_ms <= 0) call(message)
    else scheduledActionCalls += new ScheduledMessage(currentTime + delay_ms, message, 0, 0)
  }

  def call(message: Message, delay_ms: Long, repeatDelay_ms: Long): RepeatingCallHandle = {
    val handle = nextFreeTaskHandle
    nextFreeTaskHandle += 1
    scheduledActionCalls += new ScheduledMessage(currentTime + delay_ms, message, repeatDelay_ms, handle)
    new SimpleRepeatingCallHandle(this, handle)
  }

  /**
   * Should be called regularily to execute queued action calls and tasks.
   */
  def update( currentTime_ms : Long ) {

    currentTime = currentTime_ms

    // Process any scheduled actions
    var rescheduledMessages: List[ScheduledMessage] = Nil
    while (!scheduledActionCalls.isEmpty && scheduledActionCalls.max.time <= currentTime_ms) {
      val scheduledMessage: ScheduledMessage = scheduledActionCalls.dequeue

      queuedActionCalls += scheduledMessage.message

      scheduledMessage.next match {
        case Some(sm) => rescheduledMessages += sm
      }
    }
    scheduledActionCalls ++= rescheduledMessages

    // Only process the actions that have collected before this update
    val actionsToDo = queuedActionCalls
    queuedActionCalls = Nil

    // Execute all calls
    actionsToDo foreach { call : Message =>

      val headEntityId = call.calledEntity.headEntityId
      getEntity( headEntityId ) match {
        case Some(entity : Entity) => {
          call.calledEntity.tailEntityId match  {
            case Some( innerEntityId ) => entity.callContained( call )
            case None => entity.call( call )
          }
        }
        case _ => EntityLogger.logError( "Can not process action call " +call+ ", entity "+headEntityId +" not found." )
      }
    }

  }

  /**
   * A main game loop that calls update at regular intervalls.
   */
  def start() {
    while (true ) {
      update( System.currentTimeMillis )

      Thread.sleep( 1 )
    }
  }

  /**
   * Stops a repeating task with the specified handle.
   */
  def stopTask(handleToRemove: Long) {
    val retained: Iterable[ScheduledMessage] = scheduledActionCalls.filter {(sm: ScheduledMessage) => sm.handle != handleToRemove}
    scheduledActionCalls.clear
    scheduledActionCalls ++= retained
  }
}

