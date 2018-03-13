package org.gammf.proxima.dns.messages

import org.gammf.proxima.dns.util.Role._
import org.gammf.proxima.dns.util.Service.StringService

/**
  * Represents a request to the DNS service.
  * @param role the role of the actor containing the desired information. In most cases, it's a [[LeafNode]].
  * @param service the desired service.
  */
case class AddressRequestMessage(role: Role = LeafNode, service: StringService) extends DNSMessage

/**
  * It's a response to a [[AddressRequestMessage]].
  */
sealed trait AddressResponseMessage extends DNSMessage

/**
  * Represents a positive response to an [[AddressRequestMessage]].
  * Contains the address of a service that meets all the requested specifications.
  * @param address the service address.
  * @tparam A the generic type of the service address.
  */
case class AddressResponseOKMessage[A](address: A) extends AddressResponseMessage

/**
  * Represents a negative response to an [[AddressRequestMessage]].
  * Should be used when a service with all the requested specifications was not found in the DNS.
  */
case class AddressResponseErrorMessage() extends AddressResponseMessage
