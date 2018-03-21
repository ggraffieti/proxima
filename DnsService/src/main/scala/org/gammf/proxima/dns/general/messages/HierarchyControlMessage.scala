package org.gammf.proxima.dns.general.messages

import org.gammf.proxima.dns.hierarchy.messages.DNSMessage

case class HierarchyControlMessage(threshold: Int) extends DNSMessage
