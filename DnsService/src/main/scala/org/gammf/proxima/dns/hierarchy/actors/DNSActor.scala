package org.gammf.proxima.dns.hierarchy.actors

import akka.actor.Actor
import org.gammf.proxima.dns.hierarchy.util.Role._
import org.gammf.proxima.dns.hierarchy.util.Service.StringService

/**
  * Represents a simple actor of the DNS hierarchy. It's characterized by a name, a role into the DNS system and a service.
  */
trait DNSActor extends Actor{
  /**
    * Returns the name of this node.
    */
  def name: String

  /**
    * Returns the role of this node.
    */
  def role: Role

  /**
    * Returns the service offered by this node.
    */
  def service: StringService
}


