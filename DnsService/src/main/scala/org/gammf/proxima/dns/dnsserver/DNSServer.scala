package org.gammf.proxima.dns.dnsserver

import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import org.gammf.proxima.dns.general.messages._
import play.api.libs.json._

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
import scala.util.{Try, Success}

/**
  * A RESTful HTTP Server, that manage all the DNS-related request in the Proxima system.
  * The server handles requests of type GET, POST and DELETE.
  * On the address path, the clients can send a GET request, specifying a service (as path parameter), to receive the
  * address of the component that offers that service.
  * On the address path, the clients can send a POST request, to create a resource representing the address of a component
  * that offers a certain service (passed as a parameter in the request body).
  * On the address path, the clients can, also, send a DELETE request, to delete the address of a certain component
  * (passing IP address and port number as query parameters).
  */
object DNSServer {
  implicit val timeout: Timeout = Timeout(5 seconds)
  private[this] var bridgeActor: ActorRef = _
  val route: Route =
    get {
      pathPrefix(DNS_PATH / ADDRESS_PATH / Segment) {
        service => {
          println("[DNSServer] serving address request: service " + service)
          onSuccess(fetchAddress(service)) {
            case Some(address) => complete {
              println("[DNSServer] sending response: ip " + address._1 + " and port number " + address._2)
              HttpResponse(OK, entity = Json.toJson(address).toString)
            }
            case None => complete {
              println("[DNSServer] service not found, sending error response")
              HttpResponse(NotFound)
            }
          }
        }
      }
    } ~
    post {
      path(DNS_PATH / ADDRESS_PATH) {
        entity(as[String]) { jsonString =>
          println("[DNSServer] parsing address creation request")
          Try(Json.parse(jsonString)) match {
            case Success(json) => json.validate[(String, String, Int)] match {
              case info: JsSuccess[(String, String, Int)] =>
                println("[DNSServer] serving address creation request: service " + info.value._1)
                onComplete(createAddress(info.value)) { _ =>
                  println("[DNSServer] address created, sending response with url: " + COMPLETE_URL + info.value._1)
                  complete(HttpResponse(Created, headers = List(Location(COMPLETE_URL + info.value._1))))
                }
              case _ => println("[DNSServer] address creation request malformed")
                complete(HttpResponse(BadRequest))
            }
            case _ => println("[DNSServer] address creation request malformed")
              complete(HttpResponse(BadRequest))
          }
        }
      }
    } ~
    delete {
      pathPrefix(DNS_PATH / ADDRESS_PATH / Segment) { service => {
          parameters('ip, 'port.as[Int]) { (ip, port) =>
            println("[DNSServer] serving address deletion request: service " + service + ", address " + ip + ":" + port)
            onComplete(deleteAddress((service, ip, port))) { _ =>
              println("[DNSServer] address deletion completed, sending response")
              complete(HttpResponse(OK))
            }
          }
        }
      }
    }

  /**
    * This method starts the server.
    * @param actorSystem the actor system of the application.
    * @param bridgeActor the authentication actor, used to contact the internal DNS actor system.
    */
  def start(actorSystem: ActorSystem, bridgeActor: ActorRef) {
    this.bridgeActor = bridgeActor
    implicit val system: ActorSystem = actorSystem
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    Http().bindAndHandle(route, IP_ADDRESS, PORT_NUMBER)
    println(s"[DNSServer] Server online at http://$IP_ADDRESS:$PORT_NUMBER\n")
  }

  private[this] def fetchAddress(service: String): Future[Option[(String, Int)]] =
    (bridgeActor ? ExternalAddressRequestMessage(service = service)).mapTo[ExternalAddressResponseMessage].map {
      case ExternalAddressResponseOKMessage(ipAddress, port) => Some(ipAddress, port)
      case _ => None
    }

  private[this] def createAddress(info: (String, String, Int)): Future[Done] = {
    (bridgeActor ? ExternalAddressCreationRequestMessage(service = info._1, ipAddress = info._2, port = info._3))
      .mapTo[ExternalAddressCreationResponseMessage]
    Future{Done}
  }

  private[this] def deleteAddress(info: (String, String, Int)): Future[Done] = {
    (bridgeActor ? ExternalDeletionRequestMessage(service = info._1, ipAddress = info._2, port = info._3))
      .mapTo[ExternalDeletionResponseMessage]
    Future{Done}
  }
}