package org.gammf.proxima.dns.utils

/**
  * An enumeration containing all the application related roles of dns actors.
  */
object Role extends Enumeration {
  type Role = Value

  val
  /**
    * Represents the root of the Domain Name System hierarchy.
    * It can't have a parent node. It can have both [[INTERNAL_NODE]] and [[LEAF_NODE]] as children.
    */
  ROOT,
  /**
    * Represents an internal node of the Domain Name System hierarchy.
    * It can have both [[ROOT]] and [[INTERNAL_NODE]] as parent. It can have both [[INTERNAL_NODE]] and [[LEAF_NODE]] as children.
    */
  INTERNAL_NODE,
  /**
    * Represents a leaf node of the Domain Name System hierarchy.
    * It can have both [[ROOT]] and [[INTERNAL_NODE]] as parent. It can't have children.
    */
  LEAF_NODE
  = Value
}