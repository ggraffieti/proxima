package org.gammf.proxima.dns

import akka.actor.ActorRef
import org.gammf.proxima.dns.messages.{DeletionRequestMessage, RedirectionRequestMessage}
import org.gammf.proxima.dns.util.ActorDNSEntry
import org.gammf.proxima.dns.util.Role.Role
import org.gammf.proxima.dns.util.Service.StringService

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
  }
}
