package org.gammf.proxima.dns.general.actors

import org.gammf.proxima.dns.TestsUtils._
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.gammf.proxima.dns.general.messages.{AddressCreationRequestMessage, AddressCreationResponseMessage}
import org.gammf.proxima.dns.hierarchy.messages.RegistrationRequestMessage
import org.gammf.proxima.dns.utils.Role._
import org.gammf.proxima.dns.utils.Service.StringService
import org.scalatest.concurrent.Eventually
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, Matchers, WordSpecLike}

class DNSNodesCreatorActorTest extends TestKit(ActorSystem("ProximaDNS")) with ImplicitSender with WordSpecLike with BeforeAndAfterAll
  with Eventually with Matchers with BeforeAndAfterEach{

  val fakeDnsRoot: ActorRef = system.actorOf(Props(new Actor {
    override def receive: Receive = {
      case RegistrationRequestMessage(_, _, role, service) => registration = Some(role, service)
    }
  }))
  val creator: ActorRef = system.actorOf(DNSNodesCreatorActor.creatorProps(actorSystem = system, dnsRoot = fakeDnsRoot))

  var registration: Option[(Role, StringService)] = _

  override def beforeEach(): Unit = registration = None

  override def afterAll: Unit = TestKit.shutdownActorSystem(system)

  "A DNS nodes creator actor" must {
    "reply to an AddressCreationRequestMessage with an AddressCreationResponseMessage" in {
      creator ! AddressCreationRequestMessage(service = FIRST_AID_SERVICE, ipAddress = FIRST_AID_IP, port = FIRST_AID_PORT)
      expectMsg(AddressCreationResponseMessage(true))
    }
    "create the leaf node specified in the AddressCreationRequestMessage" in {
      creator ! AddressCreationRequestMessage(service = FIRST_AID_SERVICE, ipAddress = FIRST_AID_IP, port = FIRST_AID_PORT)
      eventually {
        registration should not be None
      }
      assert(registration.contains((LEAF_NODE, FIRST_AID_SERVICE)))
    }
  }
}