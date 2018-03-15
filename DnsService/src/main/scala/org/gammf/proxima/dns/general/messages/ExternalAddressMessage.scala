package org.gammf.proxima.dns.general.messages

/**
  * Represents an external request to the DNS service.
  * @param service the desired service.
  */
case class ExternalAddressRequestMessage(service: String) extends CommunicationMessage

/**
  * It's a response to a [[ExternalAddressRequestMessage]].
  */
sealed trait ExternalAddressResponseMessage extends CommunicationMessage

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
case class ExternalAddressCreationRequestMessage(service: String, ipAddress: String, port: Int) extends CommunicationMessage

/**
  * It's a response to a [[ExternalAddressCreationRequestMessage]].
  * @param result true in case of success in the address creation operation, false otherwise.
  */
case class ExternalAddressCreationResponseMessage(result: Boolean) extends CommunicationMessage