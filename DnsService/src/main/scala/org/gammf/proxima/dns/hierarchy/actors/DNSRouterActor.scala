package org.gammf.proxima.dns.hierarchy.actors

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import org.gammf.proxima.dns.hierarchy.messages._
import org.gammf.proxima.dns.utils.{ActorDNSEntry, ServiceAddress}
import org.gammf.proxima.dns.utils.Role._
import org.gammf.proxima.dns.utils.Service.StringService

import scala.concurrent.Await
import scala.annotation.tailrec
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
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

  override def receive: Receive = {
    case msg: InsertionRequestMessage => handleActorInsertion(msg)
    case msg: DeletionRequestMessage => handleActorDeletion(msg)
    case msg: AddressRequestMessage => handleAddressRequest(msg)
    case msg: HierarchyRequestMessage => handleHierarchy(msg.level)
  }

  private[this] def handleActorInsertion(msg: InsertionRequestMessage): Unit = {
    role match {
      case ROOT if !isServiceValid(msg) => sender ! InsertionErrorMessage()
      case _ => insertActor(msg)
    }
    def isServiceValid(msg: InsertionRequestMessage): Boolean = msg.service.main == service.main
    def insertActor(msg: InsertionRequestMessage): Unit = {
      searchForValidNode(msg)
      def searchForValidNode(msg: InsertionRequestMessage): Unit = dnsEntries.filter(n => n > msg && n.role == INTERNAL_NODE) match {
        case h :: _ => h.reference forward msg
        case _ => evaluateActorInsertion(msg)
      }
      def evaluateActorInsertion(msg: InsertionRequestMessage): Unit = msg.role match {
        case INTERNAL_NODE => if (dnsEntries.exists(_ === msg)) sender ! InsertionErrorMessage() else insertInternalNode(msg)
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
  }

  private[this] def handleActorDeletion(msg: DeletionRequestMessage): Unit = {
    dnsEntries.filter(_ === msg) match {
      case Nil => dnsEntries.filter(n => n > msg && n.role == INTERNAL_NODE) match {
        case h :: t => h.reference forward (msg : DeletionRequestMessage)
        case _ => sender ! DeletionResponseErrorMessage()
      }
      case list => deleteActor(list, msg.service, msg.address, sender)
    }
    def deleteActor(list: List[ActorDNSEntry], srv: StringService, addr: ServiceAddress, msgSender: ActorRef): Unit = list match {
      case h :: t => (h.reference ? AddressRequestMessage(service = srv)).mapTo[AddressResponseMessage].map {
        case AddressResponseOKMessage(a) if a == addr =>
          h.reference ! ActorDeletedMessage()
          dnsEntries = dnsEntries.filterNot(_.reference == h.reference)
          msgSender ! DeletionResponseOKMessage()
        case _ => deleteActor(t, srv, addr, msgSender)
      }
      case _ => msgSender ! DeletionResponseErrorMessage()
    }
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
    def searchForValidNode(msg: AddressRequestMessage): Unit = dnsEntries.filter(n => n > msg && n.role == INTERNAL_NODE) match {
      case h :: _ => h.reference forward msg
      case _ => sender ! AddressResponseErrorMessage()
    }
  }

  private[this] def handleHierarchy(lvl: Int): Unit = {
    role match {
      case ROOT => printHierarchy(getActors(lvl + 1))
      case _ => sender ! HierarchyResponseMessage(getActors(lvl + 1))
    }
    def getActors(level: Int): List[(Int, ActorDNSEntry)] = {
      def searchActors(actors: List[(Int, ActorDNSEntry)]): List[(Int, ActorDNSEntry)] = actors match {
        case h :: t if h._2.role != INTERNAL_NODE => h :: searchActors(t)
        case h :: t if h._2.role == INTERNAL_NODE => h :: getActorsFromChildren(h._2.reference) ++ searchActors(t)
        case _ => Nil
      }
      def getActorsFromChildren(node: ActorRef): List[(Int, ActorDNSEntry)] = Await.result(
        node ? HierarchyRequestMessage(level), timeout.duration).asInstanceOf[HierarchyResponseMessage].actors
      searchActors(dnsEntries.map((level, _)))
    }
    def printHierarchy(list: List[(Int, ActorDNSEntry)]): Unit = {
      def getRoot: HierarchyNode = HierarchyNode(level = lvl, reference = self.toString(), name = name, role = role.toString, service = service.toString)
      sender ! HierarchyNodesMessage(getRoot :: (list: List[HierarchyNode]))
    }
  }
}