package org.gammf.proxima.dns.general

import org.gammf.proxima.dns.general.messages._
import org.gammf.proxima.dns.hierarchy.messages.{AddressRequestMessage, AddressResponseOKMessage, DeletionRequestMessage}
import org.gammf.proxima.dns.utils.{Service, ServiceAddress}
import org.gammf.proxima.dns.utils.Service.StringService

import language.implicitConversions
import language.reflectiveCalls

package object actors {
  private[this] val SPLIT_CHAR: Char = '.'
  private[this] implicit def string2Service(s: String): StringService =
    s.split(SPLIT_CHAR).map(Service(_)).fold(Service())((a, b) => a ++ b)

  implicit def externalAddressRequest2AddressRequest(msg: ExternalAddressRequestMessage): AddressRequestMessage =
    AddressRequestMessage(service = msg.service)

  implicit def addressResponse2ExternalAddressResponse(msg: AddressResponseOKMessage): ExternalAddressResponseOKMessage =
    ExternalAddressResponseOKMessage(ipAddress = msg.address.ip, port = msg.address.port)

  implicit def externalAddressCreationRequest2AddressCreationRequest(msg: ExternalAddressCreationRequestMessage): AddressCreationRequestMessage =
    AddressCreationRequestMessage(service = msg.service, ipAddress = msg.ipAddress, port = msg.port)

  implicit def externalDeletionRequest2DeletionRequest(msg: ExternalDeletionRequestMessage): DeletionRequestMessage =
    DeletionRequestMessage(service = msg.service, address = ServiceAddress(ip = msg.ipAddress, port = msg.port))

  implicit def addressCreationResponse2ExternalAddressCreationResponse(msg: AddressCreationResponseMessage): ExternalAddressCreationResponseMessage =
    ExternalAddressCreationResponseMessage(result = msg.result)
}