package org.gammf.proxima.dns.general.actors

import akka.actor.{Actor, ActorRef, ActorSystem}
import org.gammf.proxima.dns.general.messages.{AddressCreationRequestMessage, AddressCreationResponseMessage}
import org.gammf.proxima.dns.hierarchy.actors.DNSNodeActor
import org.gammf.proxima.dns.utils.ServiceAddress

class DNSNodesCreatorActor(val name: String, val actorSystem: ActorSystem, val dnsRoot: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: AddressCreationRequestMessage => handleNodeCreation(msg, sender)
    case _ => println("["+ name + "] Huh?"); unhandled(_)
  }

  private[this] def handleNodeCreation(msg: AddressCreationRequestMessage, msgSender: ActorRef): Unit = {
    actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot, service = msg.service, address = ServiceAddress(msg.ipAddress, msg.port)))
    msgSender ! AddressCreationResponseMessage(true)
  }
}