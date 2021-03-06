package org.gammf.proxima.dns.hierarchy.actors

import akka.actor.{ActorRef, PoisonPill, Props}
import org.gammf.proxima.dns.hierarchy.messages._
import org.gammf.proxima.dns.utils.Role._
import org.gammf.proxima.dns.utils.Service.StringService
import org.gammf.proxima.dns.utils.ServiceAddress

/**
  * Represents a simple DNS node identified by a name and a role, offering a [[org.gammf.proxima.dns.utils.Service]].
  * In order to take part in the DNS service, this actor needs a reference to a [[DNSRootActor]].
  */
sealed trait DNSNodeActor extends DNSActor {
  /**
    * Returns a reference to the DNS root actor.
    */
  def dnsRoot: ActorRef

  override def preStart(): Unit = {
    super.preStart; println("[" + name + "] Started")
    dnsRoot ! RegistrationRequestMessage(reference = self, name = name, role = role, service = service)
  }

  override def receive: Receive = {
    case RegistrationResponseMessage() => println("[" + name + "] Registration OK.")
    case InsertionErrorMessage() => println("[" + name + "] Insertion Error."); handleDeletion()
    case ActorDeletedMessage() => println("[" + name + "] Actor Deleted."); handleDeletion()
    case _ => println("["+ name + "] Huh?"); unhandled(_)
  }

  override def postStop {
    super.postStop; println("[" + name + "] Stopped")
  }

  protected[this] def handleDeletion(): Unit = self ! PoisonPill
}

/**
  * Represents an internal node of the DNS hierarchy.
  * As every other DNS actor, this actor have to relate to the [[DNSRootActor]]. Becomes useful to create the DNS hierarchy,
  * managing all the requests regarding a certain [[org.gammf.proxima.dns.utils.Service]].
 *
  * @param name    the name of this actor.
  * @param role    the role of this actor. It should be INTERNAL_NODE.
  * @param service the service handled by this actor.
  * @param dnsRoot the reference to the DNS root actor.
  */
class DNSInternalNodeActor(override val name: String,
                           override val role: Role = INTERNAL_NODE,
                           override val service: StringService,
                           override val dnsRoot: ActorRef) extends DNSNodeActor with DNSRouterActor {
  override def receive: Receive = super[DNSRouterActor].receive orElse super[DNSNodeActor].receive
}

/**
  * Represents a leaf of the DNS hierarchy.
  * As every other DNS actor, this actor have to relate to the [[DNSRootActor]]. It manages the address of a specific service.
 *
  * @param name           the name of this actor.
  * @param role           the role of this actor. Should be LEAF_NODE.
  * @param service        the service handled by this actor.
  * @param dnsRoot        the reference to the DNS root actor.
  * @param serviceAddress the address of the service.
  */
class DNSLeafNodeActor(override val name: String,
                       override val role: Role = LEAF_NODE,
                       override val service: StringService,
                       override val dnsRoot: ActorRef,
                       val serviceAddress: ServiceAddress) extends DNSNodeActor {
  override def receive: Receive = ({
    case _: AddressRequestMessage => sender ! AddressResponseOKMessage(serviceAddress)
  }: Receive) orElse super.receive
}

object DNSNodeActor {
  /**
    * Factory methods that returns a Props to create a DNS leaf node actor offering the specified service.
    * @param dnsRoot the reference to the DNS root actor.
    * @param service the service handled by this actor.
    * @param address the address of the service.
    * @return the Props to use to create a DNS leaf node actor.
    */
  def leafNodeProps(dnsRoot: ActorRef, service: StringService, address: ServiceAddress): Props =
    Props(new DNSLeafNodeActor(name = service.last.getOrElse("") + "(" + address.toString + ")", service = service, dnsRoot = dnsRoot, serviceAddress = address))

  /**
    * Factory methods that returns a Props to create a DNS internal node actor offering the specified service.
    * @param dnsRoot the reference to the DNS root actor.
    * @param service the service handled by this actor.
    * @return the Props to use to create a DNS internal node actor.
    */
  def internalNodeProps(dnsRoot: ActorRef, service: StringService): Props = {
    Props(new DNSInternalNodeActor(name = service.last.getOrElse(""), service = service, dnsRoot = dnsRoot))
  }
}