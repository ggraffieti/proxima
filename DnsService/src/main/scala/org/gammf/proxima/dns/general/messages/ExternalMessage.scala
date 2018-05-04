package org.gammf.proxima.dns.general.messages

/**
  * Represents a generic message about the communication with the outside world.
  */
sealed trait ExternalMessage

/**
  * Represents an external request to the DNS service.
  *
  * @param service the desired service.
  */
case class ExternalAddressRequestMessage(service: String) extends ExternalMessage

/**
  * It's a response to a [[ExternalAddressRequestMessage]].
  */
sealed trait ExternalAddressResponseMessage extends ExternalMessage

/**
  * Represents a positive response to an [[ExternalAddressRequestMessage]].
  * Contains the address of a service that meets all the requested specifications.
  * @param ipAddress the service IP address.
  * @param port the service port number.
  */
case class ExternalAddressResponseOKMessage(ipAddress: String, port: Int) extends ExternalAddressResponseMessage

/**
  * Represents a negative response to an [[ExternalAddressRequestMessage]].
  * Should be used when a service with all the requested specifications was not found in the DNS.
  */
case class ExternalAddressResponseErrorMessage() extends ExternalAddressResponseMessage

/**
  * Represents an external message to the DNS service, requesting the creation of a service address.
  * This message contains all the information needed to contact an external component offering a certain service.
  * @param service the service offered by an external component.
  * @param ipAddress the ip address of the external component that offers the service.
  * @param port the port number to use to contact the external component that offers the service.
  */
case class ExternalAddressCreationRequestMessage(service: String, ipAddress: String, port: Int) extends ExternalMessage

/**
  * It's a response to a [[ExternalAddressCreationRequestMessage]].
  */
case class ExternalAddressCreationResponseMessage() extends ExternalMessage

/**
  * Represents a request about a deletion of a DNS entry coming from outside the Domain Name System, through the server.
  * @param service the service of the component to be unregistered.
  * @param ipAddress the ip address of the component to be unregistered.
  * @param port the port number of the component to be unregistered.
  */
case class ExternalDeletionRequestMessage(service: String, ipAddress: String, port: Int) extends ExternalMessage

/**
  * It's a positive response to a [[ExternalDeletionRequestMessage]].
  */
case class ExternalDeletionResponseMessage() extends ExternalMessage