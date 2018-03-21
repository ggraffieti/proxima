package org.gammf.proxima.dns.hierarchy

import akka.actor.ActorRef
import org.gammf.proxima.dns.utils.ActorDNSEntry
import org.gammf.proxima.dns.utils.Role.Role
import org.gammf.proxima.dns.utils.Service.StringService

import language.implicitConversions
import language.reflectiveCalls

package object messages {
  /**
    * Represents a generic message about a dns issue.
    */
  trait DNSMessage

  private[this] type EntryType = {
    def reference: ActorRef
    def name: String
    def role: Role
    def service: StringService
  }

  implicit def message2YellowPagesEntry(msg: EntryType): ActorDNSEntry =
    ActorDNSEntry(reference = msg.reference, name = msg.name, role = msg.role, service = msg.service)

  implicit def yellowPagesEntry2RedirectionRequest(entry: ActorDNSEntry): RedirectionRequestMessage =
    RedirectionRequestMessage(reference = entry.reference, name = entry.name, role = entry.role, service = entry.service)

  implicit def entryList2HierarchyNodeList(list: List[(Int, ActorDNSEntry)]): List[HierarchyNode] =
    list.map(node => HierarchyNode(level = node._1, reference = node._2.reference.toString, name = node._2.name,
      role = node._2.role, service = node._2.service))
}