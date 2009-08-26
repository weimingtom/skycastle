package org.skycastle.server


import com.sun.sgs.app._
import java.nio.ByteBuffer
import java.util.logging.Level


object User {

  /**
   * Gets a user with the name used in the session login, or if not found, creates a new one.
   */
  def logUserIn(session: ClientSession): User = {

    val USER_ID_PREFIX = "user-"
    val userId = USER_ID_PREFIX + session.getName()

    val dataManager = AppContext.getDataManager()

    val user: User = try {
      dataManager.getBinding(userId).asInstanceOf[User]
    } catch {
      case e: NameNotBoundException => {
        ServerLogger.logger.log(Level.INFO, "New player created: {0}", userId)
        val u = new User(userId)
        dataManager.setBinding(userId, u)
        u
      }
    }

    user.setSession( session )

    user
  }

}


/**
 * Listen to communication from one connected client.
 *
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
case class User( userId : String ) extends ClientSessionListener {

  /**The  { @code ClientSession } for this user, or null if logged out. */
  private var currentSessionRef: ManagedReference[ClientSession] = null

  def receivedMessage(message: ByteBuffer) {

  }

  def disconnected(graceful: Boolean) {

  }

  /**
   * Mark the user as logged in with the specified session
   */
  def setSession(session: ClientSession) {
    val dataManager = AppContext.getDataManager()
    dataManager.markForUpdate(this)
    currentSessionRef = dataManager.createReference(session);
    ServerLogger.logger.info("Set session for " + this + " to " + session)
  }
}

