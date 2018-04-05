package org.gammf.proxima.dns

import akka.actor.ActorSystem
import akka.pattern.ask
import org.gammf.proxima.dns.general.actors.{BridgeActor, DNSControllerActor, DNSNodesCreatorActor, PrinterActor}
import org.gammf.proxima.dns.dnsserver.DNSServer
import org.gammf.proxima.dns.general.messages.{HierarchyControlMessage, HierarchyNodesMessage, HierarchyRequestMessage}
import org.gammf.proxima.dns.hierarchy.actors.DNSRootActor

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Application entry point.
  * This object takes care of the Domain Name System hierarchy setup through the creation of its root node.
  * Furthermore, this class starts the DNS server.
  */
object EntryPoint extends App {
  val actorSystem = ActorSystem(ACTOR_SYSTEM_NAME)
  val dnsRoot = actorSystem.actorOf(DNSRootActor.rootProps(service = DNS_ROOT_SERVICE))
  val dnsNodesCreator = actorSystem.actorOf(DNSNodesCreatorActor.creatorProps(actorSystem = actorSystem, dnsRoot = dnsRoot))
  val bridge = actorSystem.actorOf(BridgeActor.bridgeProps(dnsRoot = dnsRoot, dnsNodesCreator = dnsNodesCreator))
  DNSServer.start(actorSystem = actorSystem, bridgeActor = bridge)

  val controller = actorSystem.actorOf(DNSControllerActor.controllerProps(dnsRoot = dnsRoot, dnsNodesCreator = dnsNodesCreator))
  actorSystem.scheduler.schedule(initialDelay = CONTROL_INITIAL_DELAY, interval = CONTROL_INTERVAL,
    receiver = controller, message = HierarchyControlMessage(threshold = CONTROL_THRESHOLD))

  val printer = actorSystem.actorOf(PrinterActor.printerProps())
  actorSystem.scheduler.schedule(initialDelay = PRINTING_INITIAL_DELAY, interval = PRINTING_INTERVAL) {
    (dnsRoot ? HierarchyRequestMessage()).mapTo[HierarchyNodesMessage].foreach(printer ! _)
  }
}