package org.gammf.proxima.dns.general.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.gammf.proxima.dns.general.messages.{AddressCreationRequestMessage, AddressCreationResponseMessage, ExternalAddressCreationRequestMessage, ExternalAddressCreationResponseMessage, ExternalAddressRequestMessage, ExternalAddressResponseOKMessage, ExternalDeletionRequestMessage, ExternalDeletionResponseMessage}
import org.gammf.proxima.dns.hierarchy.messages.{AddressRequestMessage, AddressResponseOKMessage, DeletionRequestMessage, DeletionResponseOKMessage}
import org.gammf.proxima.dns.utils.Role._
import org.gammf.proxima.dns.utils.Service.StringService
import org.gammf.proxima.dns.utils.ServiceAddress
import org.gammf.proxima.dns.TestsUtils._
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpecLike}
import org.scalatest.concurrent.Eventually

class BridgeActorTest extends TestKit(ActorSystem("ProximaDNS")) with ImplicitSender with WordSpecLike with BeforeAndAfterAll
  with Eventually with Matchers with BeforeAndAfterEach {

  var addressRequest: Option[(Role, StringService)] = _
  var addressCreationRequest: Option[(StringService, String, Int)] = _
  var addressDeletionRequest: Option[(StringService, Role, ServiceAddress)] = _

  val fakeDnsRoot: ActorRef = system.actorOf(Props(new Actor {
    override def receive: Receive = {
      case AddressRequestMessage(role, service) => addressRequest = Some(role, service)
        sender ! AddressResponseOKMessage(address = FIRST_AID_ADDRESS)
      case DeletionRequestMessage(service, role, address) => addressDeletionRequest = Some(service, role, address)
        sender ! DeletionResponseOKMessage()
    }
  }))
  val fakeDnsCreator: ActorRef = system.actorOf(Props(new Actor {
    override def receive: Receive = {
      case AddressCreationRequestMessage(service, ipAddress, port) => addressCreationRequest = Some(service, ipAddress, port)
        sender ! AddressCreationResponseMessage()
    }
  }))
  val bridge: ActorRef = system.actorOf(BridgeActor.bridgeProps(dnsRoot = fakeDnsRoot, dnsNodesCreator = fakeDnsCreator))

  override def beforeEach(): Unit = {
    addressRequest = None
    addressCreationRequest = None
    addressDeletionRequest = None
  }

  override def afterAll: Unit = TestKit.shutdownActorSystem(system)

  "The bridge actor, after receiving an ExternalAddressRequestMessage," must {
    "send a corresponding AddressRequestMessage to the DNS root requesting the address" in {
      bridge ! ExternalAddressRequestMessage(service = STRING_FIRST_AID_SERVICE)
      eventually {
        addressRequest should not be None
      }
      assert(addressRequest.contains((LEAF_NODE, FIRST_AID_SERVICE)))
    }
    "reply to the first message with an ExternalAddressResponseOKMessage containing the address" in {
      expectMsg(ExternalAddressResponseOKMessage(FIRST_AID_IP, FIRST_AID_PORT))
    }
  }

  "The bridge actor, after receiving an ExternalAddressCreationRequestMessage," must {
    "send a corresponding AddressCreationRequestMessage to the DNS nodes creator requesting the creation of the address" in {
      bridge ! ExternalAddressCreationRequestMessage(service = STRING_FIRST_AID_SERVICE, ipAddress = FIRST_AID_IP, port = FIRST_AID_PORT)
      eventually {
        addressCreationRequest should not be None
      }
      assert(addressCreationRequest.contains((FIRST_AID_SERVICE, FIRST_AID_IP, FIRST_AID_PORT)))
    }
    "reply to the first message with an ExternalAddressCreationResponseMessage" in {
      expectMsg(ExternalAddressCreationResponseMessage())
    }
  }

  "The bridge actor, after receiving an ExternalDeletionRequestMessage," must {
    "send a corresponding DeletionRequestMessage to the DNS root requesting the deletion of the address" in {
      bridge ! ExternalDeletionRequestMessage(service = STRING_FIRST_AID_SERVICE, ipAddress = FIRST_AID_IP, port = FIRST_AID_PORT)
      eventually {
        addressDeletionRequest should not be None
      }
      assert(addressDeletionRequest.contains((FIRST_AID_SERVICE, LEAF_NODE, FIRST_AID_ADDRESS)))
    }
    "reply to the first message with an ExternalDeletionResponseMessage" in {
      expectMsg(ExternalDeletionResponseMessage())
    }
  }
}