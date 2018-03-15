package org.gammf.proxima.dns.communication.actors

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import org.gammf.proxima.dns.communication.messages._
import org.gammf.proxima.dns.hierarchy.messages.{AddressRequestMessage, AddressResponseMessage, AddressResponseOKMessage}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

class BridgeActor(val name: String, val dnsRoot: ActorRef) extends Actor {
  implicit val timeout: Timeout = Timeout(5 seconds)

  override def receive: Receive = {
    case msg: ExternalAddressRequestMessage => handleExternalAddressRequest(msg, sender)
    case msg: ExternalAddressCreationRequestMessage => handleExternalAddressCreation(msg, sender)
    case _ => println("["+ name + "] Huh?"); unhandled(_)
  }

  private[this] def handleExternalAddressRequest(msg: ExternalAddressRequestMessage, msgSender: ActorRef): Unit = {
    (dnsRoot ? (msg: AddressRequestMessage)).mapTo[AddressResponseMessage].map {
      case res: AddressResponseOKMessage => msgSender ! (res: ExternalAddressResponseOKMessage)
      case _ => msgSender ! ExternalAddressResponseErrorMessage()
    }
  }

  private[this] def handleExternalAddressCreation(msg: ExternalAddressCreationRequestMessage, msgSender: ActorRef): Unit = {
    msgSender ! ExternalAddressCreationResponseMessage(true)
  }
}

object BridgeActor {
  /**
    * Factory methods that returns a Props to create a bridge actor.
    * @param name the name of this actor.
    * @return the Props to use to create a bridge actor.
    */
  def bridgeProps(name: String = "Bridge", dnsRoot: ActorRef): Props =
    Props(new BridgeActor(name, dnsRoot))
}