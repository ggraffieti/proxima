package org.gammf.proxima.dns.actors

import akka.util.Timeout
import akka.pattern.ask
import org.gammf.proxima.dns.messages._
import org.gammf.proxima.dns.util.ActorDNSEntry
import org.gammf.proxima.dns.util.Role._

import scala.concurrent.Await
import scala.annotation.tailrec
import scala.concurrent.duration._
import scala.language.postfixOps

/**
  * This is an actor that deals with the DNS structure.
  * Can handle registration requests and address requests, with the help of some other [[DNSRouterActor]].
  * This actor can manage even the registration of [[DNSRouterActor]] actors. In this way more DNS router actors
  * can cooperate, splitting up the work, in order to form a structured hierarchy of nodes, each one containing
  * a subset of the addresses of the services of the global DNS service.
  */
trait DNSRouterActor extends DNSActor {
  private[this] var dnsEntries: List[ActorDNSEntry] = List()
  implicit val timeout: Timeout = Timeout(5 seconds)

  import org.gammf.proxima.dns.actors.entriesImplicitConversions._
  override def receive: Receive = {
    case msg: InsertionRequestMessage => handleActorInsertion(msg)
    case msg: DeletionRequestMessage => handleActorDeletion(msg)
    case msg: AddressRequestMessage => handleAddressRequest(msg)
  }

  private[this] def handleActorInsertion(msg: InsertionRequestMessage): Unit = {
    searchForValidNode(msg)
    def searchForValidNode(msg: InsertionRequestMessage): Unit = dnsEntries.filter(n => n > msg && n.role == InternalNode) match {
      case h :: _ => h.reference forward msg
      case _ => evaluateActorInsertion(msg)
    }
    def evaluateActorInsertion(msg: InsertionRequestMessage): Unit = msg.role match {
      case InternalNode => if (dnsEntries.exists(_ === msg)) sender ! InsertionErrorMessage() else insertInternalNode(msg)
      case _ => insertSimpleActor(msg)
    }
    def insertInternalNode(msg: InsertionRequestMessage): Unit = { insertSimpleActor(msg); delegateNodesToNewNode(msg) }
    def insertSimpleActor(msg: InsertionRequestMessage): Unit = { dnsEntries = msg :: dnsEntries; sender ! buildResponse(msg) }
    def buildResponse(msg: InsertionRequestMessage): InsertionResponseMessage = msg match {
      case _: RegistrationRequestMessage => RegistrationResponseMessage()
      case RedirectionRequestMessage(ref, name, role, service) => RedirectionResponseMessage(ref, name, role, service)
    }
    def delegateNodesToNewNode(msg: InsertionRequestMessage): Unit = dnsEntries.filter(_ < msg).foreach(n => {
      Await.result(msg.reference ? (n: RedirectionRequestMessage), timeout.duration).asInstanceOf[InsertionResponseMessage] match {
        case m: RedirectionResponseMessage => dnsEntries = dnsEntries.filterNot(_ == (m: ActorDNSEntry))
        case _ => // If you don't want the actor reference it's fine, I'll keep it
      }
    })
  }

  private[this] def handleActorDeletion(msg: ActorDNSEntry): Unit = dnsEntries.filter(_ == msg) match {
    case h :: _ => dnsEntries = dnsEntries.filterNot(_ == h)
    case _ => dnsEntries.filter(n => n > msg && n.role == InternalNode).foreach(_.reference forward (msg : DeletionRequestMessage))
  }

  private[this] def handleAddressRequest(msg: AddressRequestMessage): Unit = {
    searchForActor(dnsEntries.filter(_ === msg), msg)
    def searchForActor(list: List[ActorDNSEntry], msg: AddressRequestMessage): Unit = {
      if (list.forall(_.used)) list.foreach(_.used = false)
      searchForLeastRecentlyUsedActor(list, msg)
    }
    @tailrec def searchForLeastRecentlyUsedActor(list: List[ActorDNSEntry], msg: AddressRequestMessage): Unit = list match {
      case h :: t if h.used => searchForLeastRecentlyUsedActor(t, msg)
      case h :: _ if !h.used => h.reference forward msg; h.used = true
      case _ => searchForValidNode(msg)
    }
    def searchForValidNode(msg: AddressRequestMessage): Unit = dnsEntries.filter(n => n > msg && n.role == InternalNode) match {
      case h :: _ => h.reference forward msg
      case _ => sender ! AddressResponseErrorMessage()
    }
  }
}
