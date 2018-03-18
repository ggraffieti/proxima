package org.gammf.proxima.dns.hierarchy.actors

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.gammf.proxima.dns.hierarchy.messages._
import org.gammf.proxima.dns.TestsUtils._
import org.gammf.proxima.dns.utils.Role._
import org.scalatest._

class DNSRootActorTest extends TestKit(ActorSystem("ProximaDNS")) with ImplicitSender with WordSpecLike with BeforeAndAfterAll {
  val dnsRoot: ActorRef = system.actorOf(DNSRootActor.rootProps(ROOT_SERVICE))

  override def afterAll: Unit = TestKit.shutdownActorSystem(system)

  "A just-initialized DNS root actor" must {
    "respond with an AddressResponseErrorMessage if an address is requested" in {
      dnsRoot ! AddressRequestMessage(service = FIRST_AID_SERVICE)
      expectMsgType[AddressResponseErrorMessage]
    }
  }

  "The DNS root actor" must {
    "respond with a RegistrationResponseMessage if a first aid actor asks to be registered" in {
      dnsRoot ! RegistrationRequestMessage(reference = EXAMPLE_REFERENCE, name = EXAMPLE_NAME, role = LEAF_NODE, service = EXAMPLE_SERVICE)
      expectMsgType[RegistrationResponseMessage]
    }
  }

  "The DNS root actor" must {
    "respond with an AddressResponseOKMessage if an address request (indicating the first aid service) is sent to it" in {
      system.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot, service = FIRST_AID_SERVICE, address = FIRST_AID_ADDRESS))
      Thread.sleep(100)
      dnsRoot ! AddressRequestMessage(service = FIRST_AID_SERVICE)
      expectMsg(AddressResponseOKMessage(address = FIRST_AID_ADDRESS))
    }
    "respond with an AddressResponseOKMessage even after the registration of an internal node that offers the medical service" in {
      system.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot, service = MEDICAL_SERVICE))
      Thread.sleep(100)
      dnsRoot ! AddressRequestMessage(service = FIRST_AID_SERVICE)
      expectMsg(AddressResponseOKMessage(address = FIRST_AID_ADDRESS))
    }
    "respond with an AddressResponseErrorMessage if it receives an AddressRequestMessage requesting a service different from the first aid one" in {
      dnsRoot ! AddressRequestMessage(service = EXAM_SERVICE)
      expectMsgType[AddressResponseErrorMessage]
    }
  }

  "The DNS root actor" must {
    "respond with a DeletionResponseOKMessage if it receives a DeletionRequestMessage with the first aid service" in {
      dnsRoot ! DeletionRequestMessage(service = FIRST_AID_SERVICE, address = FIRST_AID_ADDRESS)
      expectMsgType[DeletionResponseOKMessage]
    }
    "respond with an AddressResponseErrorMessage if it receives an AddressRequestMessage requesting the first aid service" in {
      dnsRoot ! AddressRequestMessage(service = FIRST_AID_SERVICE)
      expectMsgType[AddressResponseErrorMessage]
    }
    "respond with a DeletionResponseErrorMessage if it receives another DeletionRequestMessage with the first aid service" in {
      dnsRoot ! DeletionRequestMessage(service = FIRST_AID_SERVICE, address = FIRST_AID_ADDRESS)
      expectMsgType[DeletionResponseErrorMessage]
    }
  }
}