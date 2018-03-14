package org.gammf.proxima.dns

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import org.gammf.proxima.dns.actors.{DNSNodeActor, DNSRootActor}
import org.gammf.proxima.dns.messages.{AddressRequestMessage, AddressResponseMessage, AddressResponseOKMessage}
import org.gammf.proxima.dns.util.{Service, ServiceAddress}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

object EntryPoint extends App {
  implicit val timeout: Timeout = Timeout(5 seconds)

  val actorSystem = ActorSystem("ProximaDNS")
  val dnsRoot = actorSystem.actorOf(DNSRootActor.rootProps(Service() :+ "proxima"))

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
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "supermarket" :+ "conad"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "supermarket" :+ "a&o"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "restaurant" :+ "scottadito"))
  actorSystem.actorOf(DNSNodeActor.internalNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "shop" :+ "armani"))

  actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "medical" :+ "firstAid", address = ServiceAddress("192.168.0.1", 4096)))
  actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "medical" :+ "firstAid", address = ServiceAddress("192.168.0.2", 4096)))
  actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "medical" :+ "exam", address = ServiceAddress("192.168.0.3", 4096)))
  actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "supermarket" :+ "coop", address = ServiceAddress("192.168.0.4", 4096)))
  actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "supermarket" :+ "conad", address = ServiceAddress("192.168.0.5", 4096)))
  actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "supermarket" :+ "a&o", address = ServiceAddress("192.168.0.6", 4096)))
  actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "restaurant" :+ "scottadito", address = ServiceAddress("192.168.0.7", 4096)))
  actorSystem.actorOf(DNSNodeActor.leafNodeProps(dnsRoot = dnsRoot,
    service = Service() :+ "proxima" :+ "commercial" :+ "shop" :+ "armani", address = ServiceAddress("192.168.0.8", 4096)))

  Thread.sleep(1000)

  (dnsRoot ? AddressRequestMessage(service = Service() :+ "proxima" :+ "commercial" :+ "supermarket" :+ "coop"))
    .mapTo[AddressResponseMessage].map {
    case response: AddressResponseOKMessage => println(response.address)
    case _ => println("error")
  }
}
