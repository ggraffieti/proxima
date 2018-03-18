package org.gammf.proxima.dns.hierarchy.actors

import akka.actor.{Actor, ActorRef, ActorSystem, DeadLetter, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.gammf.proxima.dns.hierarchy.messages.{ActorDeletedMessage, AddressRequestMessage, AddressResponseOKMessage, InsertionErrorMessage, RedirectionRequestMessage, RedirectionResponseMessage, RegistrationRequestMessage}
import org.gammf.proxima.dns.utils.Role._
import org.gammf.proxima.dns.utils.Service.StringService
import org.gammf.proxima.dns.TestsUtils._
import org.scalatest.concurrent.Eventually
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpecLike}

class DNSNodeActorTest extends TestKit(ActorSystem("ProximaDNS")) with ImplicitSender with WordSpecLike with BeforeAndAfterAll
  with Eventually with Matchers with BeforeAndAfterEach {

  var registration: Option[(ActorRef, Role, StringService)] = _
  var messageNotDelivered: Boolean = _

  var leafNode: ActorRef = _
  var internalNode: ActorRef = _
  val fakeDnsRoot: ActorRef = system.actorOf(Props(new Actor {
    override def receive: Receive = {
      case RegistrationRequestMessage(ref, _, role, service) => registration = Some(ref, role, service)
    }
  }))
  val deadLetterMonitor: ActorRef = system.actorOf(Props(new Actor {
    override def receive: Receive = {
      case _: DeadLetter => messageNotDelivered = true
    }
  }))
  system.eventStream.subscribe(deadLetterMonitor, classOf[DeadLetter])

  override def beforeEach(): Unit = {
    registration = None
    messageNotDelivered = false
  }

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "A DNS leaf node actor" must {
    "immediately send a RegistrationRequestMessage to the DNS root actor with its correct information" in {
      leafNode = system.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = fakeDnsRoot, service = FIRST_AID_SERVICE,
        address = FIRST_AID_ADDRESS))
      eventually {
        registration should not be None
      }
      assert(registration.contains((leafNode, LEAF_NODE, FIRST_AID_SERVICE)))
    }
    "reply to an AddressRequestMessage with an appropriate AddressResponseOKMessage" in {
      leafNode ! AddressRequestMessage(service = FIRST_AID_SERVICE)
      expectMsg(AddressResponseOKMessage(address = FIRST_AID_ADDRESS))
    }
  }

  "A DNS internal node actor" must {
    "immediately send a RegistrationRequestMessage to the DNS root actor with its correct information" in {
      internalNode = system.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = fakeDnsRoot, service = MEDICAL_SERVICE))
      eventually {
        registration should not be None
      }
      assert(registration.contains((internalNode, INTERNAL_NODE, MEDICAL_SERVICE)))
    }
    "reply to a RedirectionRequestMessage with a RedirectionResponseMessage " in {
      internalNode ! RedirectionRequestMessage(reference = leafNode, name = FIRST_AID_NAME, role = LEAF_NODE, service = FIRST_AID_SERVICE)
      expectMsg(RedirectionResponseMessage(reference = leafNode, name = FIRST_AID_NAME, role = LEAF_NODE, service = FIRST_AID_SERVICE))
    }
    "reply to an AddressRequestMessage with an appropriate AddressResponseOKMessage if it contains a leaf node" in {
      internalNode ! AddressRequestMessage(service = FIRST_AID_SERVICE)
      expectMsg(AddressResponseOKMessage(address = FIRST_AID_ADDRESS))
    }
  }

  "A DNS leaf node actor" must {
    "terminate if it receives an InsertionErrorMessage" in {
      leafNode ! InsertionErrorMessage()
      eventually {
        leafNode ! "A message that should eventually encounter dead letters"
        messageNotDelivered should not be false
      }
    }
  }

  "A DNS internal node actor" must {
    "terminate if it receives an ActorDeletedMessage" in {
      internalNode ! ActorDeletedMessage()
      eventually {
        internalNode ! "A message that should eventually encounter dead letters"
        messageNotDelivered should not be false
      }
    }
  }
}