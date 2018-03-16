package org.gammf.proxima.dns.hierarchy.messages

import org.gammf.proxima.dns.utils.Role._
import org.gammf.proxima.dns.utils.Service.StringService
import org.gammf.proxima.dns.utils.ServiceAddress

/**
  * Represents a request about a deletion of a DNS entry.
  * @param service the service of the actor to be unregistered.
  * @param role the role of the actor to be unregistered.
  * @param address the address of the actor to be unregistered.
  */
case class DeletionRequestMessage(service: StringService, role: Role = LEAF_NODE, address: ServiceAddress) extends DNSMessage

trait DeletionResponseMessage extends DNSMessage

case class DeletionResponseOKMessage() extends DeletionResponseMessage

case class DeletionResponseErrorMessage() extends DeletionResponseMessage

/**
  * Represents a deletion of a DNS node actor from the DNS hierarchy.
  * This message should be sent from a [[org.gammf.proxima.dns.hierarchy.actors.DNSRouterActor]] to a
  * [[org.gammf.proxima.dns.hierarchy.actors.DNSNodeActor]] to inform the receiver of its deletion from the DNS hierarchy.
  */
case class ActorDeletedMessage() extends DNSMessage