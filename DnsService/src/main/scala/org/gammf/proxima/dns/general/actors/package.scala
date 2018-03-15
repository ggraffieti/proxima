package org.gammf.proxima.dns.general

import org.gammf.proxima.dns.general.messages.{ExternalAddressRequestMessage, ExternalAddressResponseOKMessage}
import org.gammf.proxima.dns.hierarchy.messages.{AddressRequestMessage, AddressResponseOKMessage}
import org.gammf.proxima.dns.utils.Service
import language.implicitConversions
import language.reflectiveCalls

package object actors {
  private[this] val SPLIT_CHAR: Char = '.'

  implicit def externalAddressRequest2AddressRequest(msg: ExternalAddressRequestMessage): AddressRequestMessage =
    AddressRequestMessage(service = msg.service.split(SPLIT_CHAR).map(Service(_)).fold(Service())((a, b) => a ++ b))

  implicit def addressResponse2ExternalAddressResponse(msg: AddressResponseOKMessage): ExternalAddressResponseOKMessage =
    ExternalAddressResponseOKMessage(ipAddress = msg.address.ip, port = msg.address.port)
}