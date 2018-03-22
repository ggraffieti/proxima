package org.gammf.proxima.dns

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import org.gammf.proxima.dns.general.actors.{BridgeActor, DNSControllerActor, DNSNodesCreatorActor, PrinterActor}
import org.gammf.proxima.dns.dnsserver.DNSServer
import org.gammf.proxima.dns.general.messages.{HierarchyControlMessage, HierarchyNodesMessage, HierarchyRequestMessage}
import org.gammf.proxima.dns.hierarchy.actors.DNSRootActor
import org.gammf.proxima.dns.hierarchy.messages._
import org.gammf.proxima.dns.utils.Service

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
  * Application entry point.
  * This object takes care of the Domain Name System hierarchy setup through the creation of its root node.
  * Furthermore, this class starts the DNS server.
  */
object EntryPoint extends App {
  implicit val timeout: Timeout = Timeout(5 seconds)
  val actorSystem = ActorSystem("ProximaDNS")
  val dnsRoot = actorSystem.actorOf(DNSRootActor.rootProps(Service() :+ "proxima"))
  val dnsNodesCreator = actorSystem.actorOf(DNSNodesCreatorActor.creatorProps(actorSystem = actorSystem, dnsRoot = dnsRoot))
  DNSServer.start(actorSystem, actorSystem.actorOf(BridgeActor.bridgeProps(dnsRoot = dnsRoot, dnsNodesCreator = dnsNodesCreator)))

  val controller = actorSystem.actorOf(DNSControllerActor.controllerProps(dnsRoot = dnsRoot, dnsNodesCreator = dnsNodesCreator))
  actorSystem.scheduler.schedule(initialDelay = 1 second, interval = 10 seconds,
    receiver = controller, message = HierarchyControlMessage(3))

  val printer = actorSystem.actorOf(PrinterActor.printerProps())
  actorSystem.scheduler.schedule(initialDelay = 1 second, interval = 20 seconds) {
    (dnsRoot ? HierarchyRequestMessage(0)).mapTo[HierarchyNodesMessage].foreach(printer ! _)
  }
}