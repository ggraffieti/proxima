package org.gammf.proxima.dns.general.actors

import akka.pattern.ask
import akka.util.Timeout
import akka.actor.{Actor, ActorRef, Props}
import org.gammf.proxima.dns.general.messages.{HierarchyControlMessage, InternalNodeCreationRequestMessage, InternalNodeCreationResponseMessage}
import org.gammf.proxima.dns.hierarchy.messages.{HierarchyNode, HierarchyNodesMessage, HierarchyRequestMessage}
import org.gammf.proxima.dns.utils.Role._

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

class DNSControllerActor(val name: String, val dnsRoot: ActorRef, val dnsNodesCreator: ActorRef) extends Actor {
  implicit val timeout: Timeout = Timeout(5 seconds)

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
    * @return the Props to use to create a DNS hierarchy controller actor.
    */
  def controllerProps(name: String = "HierarchyController", dnsRoot: ActorRef, dnsNodesCreator: ActorRef): Props =
    Props(new DNSControllerActor(name, dnsRoot, dnsNodesCreator))
}