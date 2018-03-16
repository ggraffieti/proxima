package org.gammf.proxima.dns

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import org.gammf.proxima.dns.general.actors.{BridgeActor, DNSNodesCreatorActor, PrinterActor}
import org.gammf.proxima.dns.dnsserver.DNSServer
import org.gammf.proxima.dns.hierarchy.actors.{DNSNodeActor, DNSRootActor}
import org.gammf.proxima.dns.hierarchy.messages._
import org.gammf.proxima.dns.utils.Service

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
  * Application entry point.
  */
object EntryPoint extends App {
  implicit val timeout: Timeout = Timeout(5 seconds)
  val actorSystem = ActorSystem("ProximaDNS")
  val dnsRoot = actorSystem.actorOf(DNSRootActor.rootProps(Service() :+ "proxima"))
  val dnsNodesCreator = actorSystem.actorOf(DNSNodesCreatorActor.creatorProps(actorSystem = actorSystem, dnsRoot = dnsRoot))
  val printer = actorSystem.actorOf(PrinterActor.printerProps())
  DNSServer.start(actorSystem, actorSystem.actorOf(BridgeActor.bridgeProps(dnsRoot = dnsRoot, dnsNodesCreator = dnsNodesCreator)))

  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "medical"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "medical" :+ "firstAid"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "medical" :+ "exam"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "supermarket"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "supermarket" :+ "coop"))
  val conad = actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "supermarket" :+ "conad"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "supermarket" :+ "a&o"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "restaurant"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "restaurant" :+ "scottadito"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "shop"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "shop" :+ "prada"))

  Thread.sleep(200)
  (dnsRoot ? HierarchyRequestMessage(0)).mapTo[HierarchyNodesMessage].foreach(printer ! _)
  Thread.sleep(200)
  Postman.main()
  Thread.sleep(200)
  (dnsRoot ? HierarchyRequestMessage(0)).mapTo[HierarchyNodesMessage].foreach(printer ! _)
}