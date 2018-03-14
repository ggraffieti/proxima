package org.gammf.proxima.dns.messages

import org.gammf.proxima.dns.util.ActorDNSEntry

/**
  * Represents a message about the hierarchical structure of the actor system.
  */
trait HierarchyMessage

/**
  * Represents a request sent to an [[org.gammf.proxima.dns.actors.DNSRouterActor]] in order to build a
  * hierarchical structure of the Domain Name System.
  * @param level a number indicating the depth level of the current structure.
  */
case class HierarchyRequestMessage(level: Int) extends HierarchyMessage

/**
  * It's a response to a [[HierarchyRequestMessage]], in which an actor shares the basic info of all the nodes that are
  * situated in a deeper level.
  * @param actors the actors situated in a deeper level compared to the sender actor.
  */
case class HierarchyResponseMessage(actors: List[(Int, ActorDNSEntry)]) extends HierarchyMessage

/**
  * Represents a message containing the hierarchical actor structure of the whole Domain Name Systems.
  * @param actors the actors that are part of the hierarchical structure.
  */
case class HierarchyNodesMessage(actors: List[HierarchyNode])

/**
  * Represents a node in a hierarchical structure.
  * @param level the depth level of the node.
  * @param reference the actor reference of the node.
  * @param name the actor name of the node.
  * @param role the actor role of the node.
  * @param service the actor service of the node.
  */
case class HierarchyNode(level: Int, reference: String, name: String, role: String, service: String)