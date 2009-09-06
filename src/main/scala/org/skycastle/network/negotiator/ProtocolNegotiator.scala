package org.skycastle.network.negotiator

import java.nio.ByteBuffer
import protocol.binary.BinaryProtocol
import protocol.{ProtocolLogger, Protocol}
import util.{StringUtils, Parameters}

/**
 * Takes care of negotiating a protocol to use with the other party.
 * @author Hans Haggstrom
 */
@serializable
@SerialVersionUID(1)
class ProtocolNegotiator( isServer : Boolean,
                          outgoingDataListener : ByteBuffer => Unit,
                          onProtocolNegotiationSuccess : (Protocol, Parameters) => Unit,
                          onProtocolNegotiationFail : (String, Parameters) => Unit ) {

  /**
   * The protocols we know about, in the order of preference.
   */
  // TODO: Maybe factories instead?  Or make sure the instantiated protocols dont use overly much space unless in use.
  val supportedProtocols : List[Protocol] = List( new BinaryProtocol )

  private var waitingForSelection = false
  private var negotiationOver = false
  private var storedServerParams : Parameters = null
  private var negotiationStarted = false

  private val PROTOCOL_SEPARATOR = ','

  /**
   * Start protocol negotiations, if it is up to this party to do that.
   */
  def start() {
    if (!negotiationStarted) {
      negotiationStarted = true

      // The server is the one who starts the exchange
      if ( isServer ) {

        // TODO: Get the properties from the application properties or similar?
        val application = "Skycastle"
        val version     = "0.1.0"
        val build       = "r?"
        val serverName  = "Skycastle Test Server"
        val serverDescription  = "For testing the Skycastle Server.  May reboot at any time."

        send( Parameters(
          'application -> stripRowSeparators( application ),
          'version     -> stripRowSeparators( version ),
          'build       -> stripRowSeparators( build ),
          'name        -> stripRowSeparators( serverName ),
          'description -> stripRowSeparators( serverDescription ),
          'supportedProtocols -> stripRowSeparators( supportedProtocolsAsString ) ) )
      }
    }
    else {
      throw new IllegalStateException( "Negotiation is already started, but start was called on a ProtocolNegotiator." )
    }
  }

  /**
   * Stop protocol negotiations, allowing them to be started again later.
   */
  def stop() {
    if (negotiationStarted) {
      negotiationStarted = false
      waitingForSelection = false
      negotiationOver = false
      storedServerParams = null
    }
  }

  def handleData( buffer : ByteBuffer ) {

    if (!negotiationOver) {
      val parameters : Parameters = Parameters.fromKeyValueString( StringUtils.decodeString( buffer ) )

      if ( isServer ) handleServer( parameters )
      else handleClient( parameters )
    }
    else {
      ProtocolLogger.logInfo( "Negotiation is over, but we still got a "+buffer.capacity+" bytes sized datapackage to the ProtocolNegotiator.  Ignoring it." )
    }

  }

  private def handleServer( parameters : Parameters ) {

    negotiationOver = true

    val suggestedProtocols : List[String]= List.fromString( parameters.getString( 'supportedProtocols, "" ), PROTOCOL_SEPARATOR) map ( _.trim )

    supportedProtocols.find( { p : Protocol => suggestedProtocols.exists( _ == p.identifier.name ) } ) match {
      case Some( protocol : Protocol ) => {

        // We found a protocol that we both speak, notify client and listeners
        send( Parameters( 'selectedProtocol -> protocol.identifier.name ) )
        onProtocolNegotiationSuccess( protocol, parameters )

      }
      case None => {

        // We didn't find any matching protocol.
        send( Parameters( 'selectedProtocol -> "" ) )
        onProtocolNegotiationFail( "No common protocol found.", parameters )
      }
    }
  }

  private def handleClient( parameters : Parameters ) {

    if (waitingForSelection) {
      negotiationOver = true

      val selectedProtocol = Symbol( parameters.getString( 'selectedProtocol, null ) )
      supportedProtocols.find( _.identifier == selectedProtocol ) match {
        case Some(protocol : Protocol) => {
          // Server selected a protocol, and we have it -> success
          onProtocolNegotiationSuccess( protocol, storedServerParams )
        }
        case None => {
          // Server indicated some non-existing protocol, or didn't find any suitable one -> failure
          onProtocolNegotiationFail( "No common protocol found.", storedServerParams )
        }
      }
    }
    else {
      waitingForSelection = true

      // Store server parameters
      storedServerParams = parameters

      // Send client info to server

      // TODO: Get the properties from the application properties or similar?
      val application = "SkycastleClient"
      val version     = "0.1.0"
      val build       = "r?"

      send( Parameters(
        'application -> application,
        'version     -> version,
        'build       -> build,
        'supportedProtocols -> supportedProtocolsAsString ) )
    }
  }



  /**
   * For avoiding mess ups if some properties happen to contain newlines.
   */
  private def stripRowSeparators( s : String ) : String = s.replace( "\n", "   " )


  private def send( parameters : Parameters ) {

    outgoingDataListener( StringUtils.encodeString( parameters.toKeyValueString( " = ", "\n" ) ) )
  }



  private def supportedProtocolsAsString = (supportedProtocols map { _.identifier.name }).mkString( " "+PROTOCOL_SEPARATOR+" " )


}