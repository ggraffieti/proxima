package org.gammf.proxima.dns.dnsserver

import akka.http.scaladsl.model.{HttpMethods, HttpRequest, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.actor.{Actor, ActorRef, Props}
import akka.testkit.TestKit
import org.gammf.proxima.dns.general.messages._
import org.gammf.proxima.dns.TestsUtils._
import org.scalatest.concurrent.Eventually
import org.scalatest._

class DNSServerTest extends WordSpec with Matchers with Eventually with ScalatestRouteTest with BeforeAndAfterEach {

  var addressRequest: Option[String] = _
  var addressCreation: Option[(String, String, Int)] = _
  var addressDeletion: Option[(String, String, Int)] = _

  val fakeBridge: ActorRef = system.actorOf(Props(new Actor {
    override def receive: Receive = {
      case ExternalAddressRequestMessage(service) => addressRequest = Some(service); service match {
        case STRING_FIRST_AID_SERVICE => sender ! ExternalAddressResponseOKMessage(FIRST_AID_IP, FIRST_AID_PORT)
        case _ => sender ! ExternalAddressResponseErrorMessage()
      }
      case ExternalAddressCreationRequestMessage(service, ip, port) => addressCreation = Some((service, ip, port))
        sender ! ExternalAddressCreationResponseMessage()
      case ExternalDeletionRequestMessage(service, ip, port) => addressDeletion = Some((service, ip, port))
        sender ! ExternalDeletionResponseMessage()
    }
  }))

  val postRequest = HttpRequest (
    HttpMethods.POST,
    uri = FIRST_AID_POST_PATH,
    entity = FIRST_AID_POST_REQUEST
  )
  val wrongPostRequest = HttpRequest (
    HttpMethods.POST,
    uri = FIRST_AID_POST_PATH,
    entity = "some wrong data"
  )

  override def beforeAll(): Unit = {
    DNSServer.start(system, fakeBridge)
  }

  override def beforeEach(): Unit = {
    addressRequest = None
    addressCreation = None
    addressDeletion = None
  }

  override def afterAll: Unit = TestKit.shutdownActorSystem(system)

  "The DNS server" must {
    "send an ExternalAddressRequestMessage to the bridge actor, after receiving a GET request" in {
      Get(FIRST_AID_GET_PATH) ~> DNSServer.route ~> check {
        eventually {
          addressRequest should not be None
        }
        assert(addressRequest.contains(STRING_FIRST_AID_SERVICE))
      }
    }
    "reply to a GET request with the appropriate address, if present" in {
      Get(FIRST_AID_GET_PATH) ~> DNSServer.route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual FIRST_AID_GET_RESPONSE
      }
    }
    "reply to a GET request with an error, if the address is not present" in {
      Get(EXAM_GET_PATH) ~> DNSServer.route ~> check {
        status shouldEqual StatusCodes.NotFound
      }
    }
  }

  "The DNS server" must {
    "send an ExternalAddressCreationCreationMessage to the bridge actor, after receiving a POST request" in {
      postRequest ~> DNSServer.route ~> check {
        eventually {
          addressCreation should not be None
        }
        assert(addressCreation.contains((STRING_FIRST_AID_SERVICE, FIRST_AID_IP, FIRST_AID_PORT)))
      }
    }
    "reply to a POST request with the appropriate Location header" in {
      postRequest ~> DNSServer.route ~> check {
        status shouldEqual StatusCodes.Created
        header("Location").map(_.toString).contains(COMPLETE_URL + "/" + FIRST_AID_SERVICE)
      }
    }
    "reply to a malformed POST request with the appropriate error" in {
      wrongPostRequest ~> DNSServer.route ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }
  }

  "The DNS server" must {
    "send an ExternalDeletionRequestMessage to the bridge actor, after receiving a DELETE request" in {
      Delete(FIRST_AID_DELETE_PATH) ~> DNSServer.route ~> check {
        eventually {
          addressDeletion should not be None
        }
        assert(addressDeletion.contains((STRING_FIRST_AID_SERVICE, FIRST_AID_IP, FIRST_AID_PORT)))
      }
    }
    "reply to a DELETE request" in {
      Delete(FIRST_AID_DELETE_PATH) ~> DNSServer.route ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
  }
}