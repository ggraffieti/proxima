package org.gammf.proxima.dns.general.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import org.gammf.proxima.dns.general.messages._
import org.gammf.proxima.dns.hierarchy.messages._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This class represents a "bridge" between the internal Domain Name System and the external world passing through the server.
  * This object is the only actor reachable from the server. Every external request arrived at the server must pass from
  * this actor that can forward it appropriately to the internal DNS hierarchy.
  * @param name the name of this actor.
  * @param dnsRoot the reference to the [[org.gammf.proxima.dns.hierarchy.actors.DNSRootActor]] of the DNS hierarchy.
  * @param dnsNodesCreator the reference to the [[DNSNodesCreatorActor]].
  */
class BridgeActor(val name: String, val dnsRoot: ActorRef, val dnsNodesCreator: ActorRef) extends Actor {

  override def receive: Receive = {
    case msg: ExternalAddressRequestMessage => handleExternalAddressRequest(msg, sender)
    case msg: ExternalAddressCreationRequestMessage => handleExternalAddressCreation(msg, sender)
    case msg: ExternalDeletionRequestMessage => handleExternalDeletionRequest(msg, sender)
    case _ => println("["+ name + "] Huh?"); unhandled(_)
  }

  private[this] def handleExternalAddressRequest(msg: ExternalAddressRequestMessage, msgSender: ActorRef): Unit = {
    (dnsRoot ? (msg: AddressRequestMessage)).mapTo[AddressResponseMessage].map {
      case res: AddressResponseOKMessage => msgSender ! (res: ExternalAddressResponseOKMessage)
      case _ => msgSender ! ExternalAddressResponseErrorMessage()
    }
  }

  private[this] def handleExternalAddressCreation(msg: ExternalAddressCreationRequestMessage, msgSender: ActorRef): Unit = {
    (dnsNodesCreator ? (msg: AddressCreationRequestMessage)).mapTo[AddressCreationResponseMessage].map { res =>
      msgSender ! (res: ExternalAddressCreationResponseMessage)
    }
  }

  private[this] def handleExternalDeletionRequest(msg: ExternalDeletionRequestMessage, msgSender: ActorRef): Unit = {
    (dnsRoot ? (msg: DeletionRequestMessage)).mapTo[DeletionResponseMessage].map { _ =>
      msgSender ! ExternalDeletionResponseMessage()
    }
  }
}

object BridgeActor {
  /**
    * Factory methods that returns a Props to create a bridge actor.
    * @param name the name of the bridge actor.
    * @return the Props to use to create a bridge actor.
    */
  def bridgeProps(name: String = "Bridge", dnsRoot: ActorRef, dnsNodesCreator: ActorRef): Props =
    Props(new BridgeActor(name, dnsRoot, dnsNodesCreator))
}