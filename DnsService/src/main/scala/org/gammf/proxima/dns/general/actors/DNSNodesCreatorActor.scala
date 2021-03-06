package org.gammf.proxima.dns.general.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import org.gammf.proxima.dns.general.messages._
import org.gammf.proxima.dns.hierarchy.actors.DNSNodeActor
import org.gammf.proxima.dns.utils.ServiceAddress

/**
  * This is an actor that handles the creation of actors of type [[org.gammf.proxima.dns.hierarchy.actors.DNSNodeActor]].
  * It can create both leaf nodes and internal nodes of the DNS hierarchy.
  * @param name the name of this actor.
  * @param actorSystem the actor system, necessary for the nodes creation.
  * @param dnsRoot the reference to the root node actor, that needs to be passed to all the created nodes actor.
  */
class DNSNodesCreatorActor(val name: String, val actorSystem: ActorSystem, val dnsRoot: ActorRef) extends Actor {
  override def receive: Receive = {
    case msg: AddressCreationRequestMessage => handleLeafNodeCreation(msg, sender)
    case msg: InternalNodeCreationRequestMessage => handleInternalNodeCreation(msg, sender)
    case _ => println("["+ name + "] Huh?"); unhandled(_)
  }

  private[this] def handleLeafNodeCreation(msg: AddressCreationRequestMessage, msgSender: ActorRef): Unit = {
    actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot, service = msg.service, address = ServiceAddress(msg.ipAddress, msg.port)))
    msgSender ! AddressCreationResponseMessage()
  }

  private[this] def handleInternalNodeCreation(msg: InternalNodeCreationRequestMessage, msgSender: ActorRef): Unit = {
    actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot, service = msg.service))
    msgSender ! InternalNodeCreationResponseMessage()
  }
}

object DNSNodesCreatorActor {
  /**
    * Factory methods that returns a Props to create a dns nodes creator actor.
    * @param name the name of the actor.
    * @return the Props to use to create a dns nodes creator actor.
    */
  def creatorProps(name: String = "NodesCreator", actorSystem: ActorSystem, dnsRoot: ActorRef): Props =
    Props(new DNSNodesCreatorActor(name = name, actorSystem = actorSystem, dnsRoot = dnsRoot))
}