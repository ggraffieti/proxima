package org.gammf.proxima.dns

import akka.actor.ActorRef
import org.gammf.proxima.dns.hierarchy.messages.{DeletionRequestMessage, HierarchyNode, RedirectionRequestMessage}
import org.gammf.proxima.dns.hierarchy.util.ActorDNSEntry
import org.gammf.proxima.dns.hierarchy.util.Role.Role
import org.gammf.proxima.dns.hierarchy.util.Service.StringService

package object actors {
  object entriesImplicitConversions {
    import language.implicitConversions
    import language.reflectiveCalls

    private[this] type EntryType = {
      def reference: ActorRef
      def name: String
      def role: Role
      def service: StringService
    }

    implicit def message2yellowPagesEntry(msg: EntryType): ActorDNSEntry =
      ActorDNSEntry(reference = msg.reference, name = msg.name, role = msg.role, service = msg.service)

    implicit def yellowPagesEntry2RedirectionRequest(entry: ActorDNSEntry): RedirectionRequestMessage =
      RedirectionRequestMessage(reference = entry.reference, name = entry.name, role = entry.role, service = entry.service)

    implicit def yellowPagesEntry2DeleteRequest(entry: ActorDNSEntry): DeletionRequestMessage =
      DeletionRequestMessage(reference = entry.reference, name = entry.name, role = entry.role, service = entry.service)

    implicit def entryList2hierarchyNodeList(list: List[(Int, ActorDNSEntry)]): List[HierarchyNode] =
      list.map(node => HierarchyNode(level = node._1, reference = node._2.reference.toString, name = node._2.name.toString,
        role = node._2.role.toString, service = node._2.service.toString))
  }
}
