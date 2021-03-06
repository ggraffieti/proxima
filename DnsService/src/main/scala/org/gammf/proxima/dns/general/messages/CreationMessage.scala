package org.gammf.proxima.dns.general.messages

import org.gammf.proxima.dns.hierarchy.messages.DNSMessage
import org.gammf.proxima.dns.utils.Service.StringService

/**
  * Represents a message to the DNS service, requesting the creation of a service address.
  * This message contains all the information needed to contact an external component offering a certain service.
 *
  * @param service the service offered by an external component.
  * @param ipAddress the ip address of the external component that offers the service.
  * @param port the port number to use to contact the external component that offers the service.
  */
case class AddressCreationRequestMessage(service: StringService, ipAddress: String, port: Int) extends DNSMessage

/**
  * It's a positive response to a [[AddressCreationRequestMessage]].
  */
case class AddressCreationResponseMessage() extends DNSMessage

/**
  * Represents a message to the DNS service, requesting the creation of an internal node.
  * @param service the service handled by the node.
  */
case class InternalNodeCreationRequestMessage(service: StringService) extends DNSMessage

/**
  * It's a positive response to a [[InternalNodeCreationRequestMessage]].
  */
case class InternalNodeCreationResponseMessage() extends DNSMessage