package org.skycastle.content.activities.activitybrowser


import activity.ActivityEntity
import entity.accesscontrol.users
import entity.{parameters, EntityId, Entity}
import factory.EntityFactory
import util.{Parameters}
import util.ParameterChecker._

/**
 * Allows users to browse through a list of ongoing games, join one, or start a new one.
 *
 * Used as an entrypoint in a server, but can also be used in-game for sub-games in some place.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ActivityBrowser extends ActivityEntity {

  private var activityTypes: Map[Symbol, (EntityId, Parameters)] = Map()

  private var activities: Map[EntityId, Parameters] = Map()

  @users("activityMember")
  @parameters("$callerId, activityType, activityParameters, userParameters")
  def createActivity(user: EntityId, activityType: Symbol, activityParameters: Parameters, joinParameters: Parameters) {
    requireNotNull(user, 'user)
    requireNotNull(activityType, 'gameType)
    requireNotNull(activityParameters, 'gameParameters)

    // TODO: Check access rights to create the specified type of activity etc

    // Find and create the activity
    activityTypes.get(activityType) match {
      case Some(x) =>
        val factoryId: EntityId = x._1
        val info: Parameters = x._2

        container.getEntity(factoryId) match {
          case Some(factory: EntityFactory) =>

            val entity: Entity = factory.createEntity(activityParameters)

            if (classOf[ActivityEntity].isAssignableFrom(entity.getClass())) {

              val activity: ActivityEntity = entity.asInstanceOf[ActivityEntity]
              val activityId: EntityId = activity.id
              val parameters = Parameters()

              // Listen to status updates from the activity
              activity.addStatusListener(id)

              // Join the user into the activity
              activity.joinActivity(user, joinParameters)

              // Add the activity to the activitylist
              val entry = (activityId, parameters)
              activities = activities + entry

              // and notify our members
              getMembers foreach {member: EntityId => notifyMemberOfUpdate(member, activityId, parameters)}
            }
            else {
              logWarning("The activity type with id '" + activityType.name + "' did not create ActivityEntity instances.  Removing it.")
              container.removeEntity(entity.id)
              removeActivityType(activityType)
            }
          case _ => logWarning("No activity factory with id '" + factoryId + "' found.")
        }

      case None => logWarning("Can not create activity of type '" + activityType.name + "', activity type not found.")
    }
  }


  def removeActivity(activityId: EntityId) {
    requireNotNull(activityId, 'activityId)

    activities.get(activityId) match {
      case Some(info) => {
        activities = activities - activityId

        getMembers foreach {member: EntityId => notifyMemberOfRemoval(member, activityId, info)}
      }
      case None => logWarning("Attempt to remove non-existing activity '" + activityId + "'.")
    }
  }

  def addActivityType(activityTypeID: Symbol, activityFactory: EntityId) {
    requireNotNull(activityTypeID, 'activityTypeID)
    requireNotNull(activityFactory, 'activityFactory)

    container.getEntity(activityFactory) match {
      case Some(factory: EntityFactory) =>

        val info = factory.info

        val entry = (activityTypeID, (activityFactory, info))
        activityTypes = activityTypes + entry

        getMembers foreach {m => notifyMemberOfAddedActivityType(m, activityTypeID, info)}

      case _ => logWarning("No activity factory with id '" + activityFactory + "' found.")
    }

  }

  def removeActivityType(activityTypeID: Symbol) {
    requireNotNull(activityTypeID, 'activityTypeID)

    if (activityTypes.contains(activityTypeID)) {
      activityTypes = activityTypes - activityTypeID

      getMembers foreach {m => notifyMemberOfRemovedActivityType(m, activityTypeID)}
    }
  }

  def activityStatusUpdate(activityId: EntityId, parameters: Parameters) {
    requireNotNull(activityId, 'activityId)
    requireNotNull(parameters, 'parameters)

    activities.get(activityId) match {
      case Some(oldInfo) => {
        val newEntry = (activityId, parameters)
        activities = activities + newEntry

        getMembers foreach {member: EntityId => notifyMemberOfUpdate(member, activityId, parameters)}
      }
      case None => logWarning("Status update received from the entity '" + activityId + "', which is not in the Activity list.")
    }
  }


  override protected def onMemberJoined(member: EntityId, joinParameters: Parameters) {
    activityTypes foreach {
      case (activityType: Symbol, ( factoryID : EntityId, info: Parameters ) )  =>
        notifyMemberOfAddedActivityType(member, activityType, info)
    }
    activities foreach {
      case (activityId: EntityId, info: Parameters) =>
        notifyMemberOfUpdate(member, activityId, info)
    }
  }


  override def createClientUi(client: EntityId) {

    // Collecton with activities & statuses - store in list properties with properties in the client object
    // Collection with available activity types

    // List of activities, showing activity collection.  Specify columns to show, and what data to put in them, and how to visualize that data.

    // Join button next to it, for joining the selected activity (only activate button when activity selected)
    // Can also doubleclick activity for join

    // Drop down selector with available activity types
    // Create button next to it

    // Listen to changes to activities, update activity collection

    // Listen to changes to available activity types, update ui

  }

  private def notifyMemberOfUpdate(member: EntityId, activityId: EntityId, activityInfo: Parameters) {
    callOtherEntity(member, 'activityUpdated, Parameters('activityId -> activityId, 'info -> activityInfo))
  }

  private def notifyMemberOfRemoval(member: EntityId, activityId: EntityId, endStatus: Parameters) {
    callOtherEntity(member, 'activityRemoved, Parameters('activityId -> activityId, 'info -> endStatus))
  }

  private def notifyMemberOfAddedActivityType(member: EntityId, activityTypeID: Symbol, activityTypeInfo: Parameters) {
    callOtherEntity(member, 'activityTypeAdded, Parameters('activityTypeId -> activityTypeID, 'info -> activityTypeInfo))
  }

  private def notifyMemberOfRemovedActivityType(member: EntityId, activityTypeID: Symbol) {
    callOtherEntity(member, 'activityTypeRemoved, Parameters('activityTypeID -> activityTypeID))
  }

}

