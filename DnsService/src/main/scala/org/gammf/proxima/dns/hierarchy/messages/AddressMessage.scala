package org.gammf.proxima.dns.hierarchy.messages

import org.gammf.proxima.dns.utils.Role._
import org.gammf.proxima.dns.utils.Service.StringService
import org.gammf.proxima.dns.utils.ServiceAddress

/**
  * Represents a request to the DNS service.
  * @param role    the role of the actor containing the desired information. In most cases, it's a [[LEAF_NODE]].
  * @param service the desired service.
  */
case class AddressRequestMessage(role: Role = LEAF_NODE, service: StringService) extends DNSMessage

/**
  * It's a response to a [[AddressRequestMessage]].
  */
sealed trait AddressResponseMessage extends DNSMessage

/**
  * Represents a positive response to an [[AddressRequestMessage]].
  * Contains the address of a service that meets all the requested specifications.
  * @param address the service address.
  */
case class AddressResponseOKMessage(address: ServiceAddress) extends AddressResponseMessage

/**
  * Represents a negative response to an [[AddressRequestMessage]].
  * Should be used when a service with all the requested specifications was not found in the DNS.
  */
case class AddressResponseErrorMessage() extends AddressResponseMessage