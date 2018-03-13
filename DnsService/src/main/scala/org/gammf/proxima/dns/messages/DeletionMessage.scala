package org.gammf.proxima.dns.messages

import akka.actor.ActorRef
import org.gammf.proxima.dns.util.Role.Role
import org.gammf.proxima.dns.util.Service.StringService

/**
  * Represents a request about a deletion of a DNS entry.
  * @param reference the reference of the actor to be unregistered.
  * @param name the name of the actor to be unregistered.
  * @param role the actor role.
  * @param service the actor service.
  */
case class DeletionRequestMessage(reference: ActorRef, name: String, role: Role, service: StringService) extends DNSMessage