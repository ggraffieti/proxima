package org.gammf.proxima.dns.general.actors

import akka.pattern.ask
import akka.actor.{Actor, ActorRef, Props}
import org.gammf.proxima.dns.general.messages._
import org.gammf.proxima.dns.utils.Role._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * This actor can analyze the DNS hierarchy structure and act accordingly.
  * If the hierarchy lacks of internal nodes presence, this actor can demand the creation of an appropriate
  * [[org.gammf.proxima.dns.hierarchy.actors.DNSInternalNodeActor]] to a [[DNSNodesCreatorActor]].
  * @param name the name of this actor.
  * @param dnsRoot the reference to the DNS root node, useful to build the current DNS hierarchy.
  * @param dnsNodesCreator the reference to a DNS nodes creator actor, useful to demand the internal nodes creation.
  */
class DNSControllerActor(val name: String, val dnsRoot: ActorRef, val dnsNodesCreator: ActorRef) extends Actor {

  override def receive: Receive = {
    case HierarchyControlMessage(threshold: Int) =>
      (dnsRoot ? HierarchyRequestMessage(0)).mapTo[HierarchyNodesMessage].foreach(handleHierarchy(_, threshold))
    case _ => println("["+ name + "] Huh?"); unhandled(_)
  }

  private[this] def handleHierarchy(msg: HierarchyNodesMessage, threshold: Int): Unit = {
    for (i <- 1 to msg.actors.map(_.service.size()).max) yield handleHierarchyLevel(msg.actors, i)
    def handleHierarchyLevel(list: List[HierarchyNode], lvl: Int): Unit = list
      .filter(_.role == LEAF_NODE)
      .groupBy(_.service.getInBetween(0, lvl))
      .filterNot(fn => list.exists(sn => sn.role == INTERNAL_NODE && sn.service == fn._1))
      .filter(_._2.lengthCompare(threshold) >= 0)
      .foreach(n => (dnsNodesCreator ? InternalNodeCreationRequestMessage(n._1)).mapTo[InternalNodeCreationResponseMessage])
  }
}

object DNSControllerActor {
  /**
    * Factory methods that returns a Props to create a DNS hierarchy controller actor.
    * @param name the name of the DNS hierarchy controller actor.
    * @param dnsRoot the reference to the root node actor of the DNS hierarchy.
    * @param dnsNodesCreator the reference to a DNS nodes creator actor.
    * @return the Props to use to create a DNS hierarchy controller actor.
    */
  def controllerProps(name: String = "HierarchyController", dnsRoot: ActorRef, dnsNodesCreator: ActorRef): Props =
    Props(new DNSControllerActor(name = name, dnsRoot = dnsRoot, dnsNodesCreator = dnsNodesCreator))
}