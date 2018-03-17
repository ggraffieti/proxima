package org.gammf.proxima.dns.hierarchy.actors

import akka.actor.Props
import org.gammf.proxima.dns.utils.Role._
import org.gammf.proxima.dns.utils.Service.StringService

/**
  * Represents the master of the [[DNSRouterActor]] actors.
  * Every DNS related request from any actor should be sent to this actor, in particular registration requests and address requests.
  * A complex Domain Name System should be based on a structured service, built on multiple levels. Despite this,
  * every actor of the system should only interact with this actor, that will forward all the requests to the dns nodes
  * of the appropriate level.
 *
  * @param name    the name of this actor.
  * @param role    the role of this actor. It should be [[ROOT]].
  * @param service the service offered by this actor. It should be the most general service of the DNS system.
  */
class DNSRootActor(override val name: String,
                   override val role: Role = ROOT,
                   override val service: StringService) extends DNSRouterActor {
  override def receive: Receive = super[DNSRouterActor].receive orElse {
    case _ => println("["+ name + "] Huh?"); unhandled(_)
  }
}

object DNSRootActor {
  /**
    * Factory method that returns a Props to create the DNS root actor.
    * @param service the service offered by this actor. It should be the most general service of the DNS system.
    * @return the Props to use to create the DNS root actor.
    */
  def rootProps(service: StringService): Props =
    Props(new DNSRootActor(name = service.toString, service = service))
}