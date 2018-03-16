package org.gammf.proxima.dns.general.messages

import org.gammf.proxima.dns.hierarchy.messages.DNSMessage

/**
  * Represents a request about a deletion of a DNS entry coming from outside the Domain Name System, through the server.
  * @param service the service of the component to be unregistered.
  * @param ipAddress the ip address of the component to be unregistered.
  * @param port the port number of the component to be unregistered.
  */
case class ExternalDeletionRequestMessage(service: String, ipAddress: String, port: Int) extends DNSMessage

/**
  * It's a response to a [[ExternalDeletionRequestMessage]].
  */
case class ExternalDeletionResponseMessage() extends DNSMessage