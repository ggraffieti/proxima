package org.gammf.proxima.dns.util

/**
  * An enumeration containing all the application related roles of dns actors.
  */
object Role extends Enumeration {
  type Role = Value
  
  val Root,
  InternalNode,
  LeafNode
  = Value
}