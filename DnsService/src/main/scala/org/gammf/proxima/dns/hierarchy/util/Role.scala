package org.gammf.proxima.dns.hierarchy.util

/**
  * An enumeration containing all the application related roles of dns actors.
  */
object Role extends Enumeration {
  type Role = Value

  val
  /**
    * Represents the root of the Domain Name System hierarchy.
    * It can't have a parent node. It can have both [[InternalNode]] and [[LeafNode]] as children.
    */
  Root,
  /**
    * Represents an internal node of the Domain Name System hierarchy.
    * It can have both [[Root]] and [[InternalNode]] as parent. It can have both [[InternalNode]] and [[LeafNode]] as children.
    */
  InternalNode,
  /**
    * Represents a leaf node of the Domain Name System hierarchy.
    * It can have both [[Root]] and [[InternalNode]] as parent. It can't have children.
    */
  LeafNode
  = Value
}