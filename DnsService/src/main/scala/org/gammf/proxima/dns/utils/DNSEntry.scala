package org.gammf.proxima.dns.utils

import akka.actor.ActorRef
import org.gammf.proxima.dns.utils.Role._
import org.gammf.proxima.dns.utils.Service.StringService

import language.reflectiveCalls

/**
  * Represents a generic Domain Name System entry.
  * Contains the entity reference, the entity role in the system and the service offered by the entity.
  * @tparam A the generic type used to reference the entity.
  * @tparam B the generic type used to express the role.
  * @tparam C the generic type used to describe the service.
  */
sealed trait DNSEntry[A, B, C] {
  /**
    * Returns the reference to the entity registered to the service.
    */
  def reference: A

  /**
    * Returns the role of the entity into the system.
    */
  def role: B

  /**
    * Returns the service offered by the entity.
    */
  def service: C

  /**
    * Returns a boolean flag stating if the entry has been used in the recent time.
    * @return true if the entry has been used recently, false otherwise.
    */
  def used: Boolean

  /**
    * Sets a boolean flag stating if the entry has been used in the recent time.
    * @param used a boolean flag stating if the entry has been used recently.
    */
  def used_= (used: Boolean): Unit
}

/**
  * Represents a DNS entry in the actor world.
  * Contains the actor reference, the actor name, the actor [[Role]] into the system and the [[Service]] offered by the actor.
  */
sealed trait ActorDNSEntry extends DNSEntry[ActorRef, Role, StringService] {
  protected[this] type EntryParam = {
    def role: Role
    def service: StringService
  }

  /**
    * Returns the name of the actor.
    */
  def name: String

  /**
    * Similarity method. Compares this entry to some other entry.
    * @param that the other entry to be compared to this entry.
    * @return true if the given entry is similar to this entry, false otherwise.
    */
  def ===(that: EntryParam): Boolean

  /**
    * Greater method. Checks if this entry is greater than another object, based on its topic value.
    * @param that the object to be compared to this entry.
    * @return true if this entry is greater than the given object, false otherwise.
    */
  def >(that: EntryParam): Boolean

  /**
    * Greater or similar method. Checks if this entry is similar or greater than another object, based on its topic value.
    * @param that the object to be compared to this entry.
    * @return true if this entry is similar or greater than the given object, false otherwise.
    */
  def >=(that: EntryParam): Boolean

  /**
    * Lesser method. Checks if this entry is lesser than another object, based on its topic value.
    * @param that the object to be compared to this entry.
    * @return true if this entry is lesser than the given object, false otherwise.
    */
  def <(that: EntryParam): Boolean

  /**
    * Lesser or similar method. Checks if this entry is similar or lesser than another object, based on its topic value.
    * @param that the object to be compared to this entry.
    * @return true if this entry is similar or lesser than the given object, false otherwise.
    */
  def <=(that: EntryParam): Boolean
}

/**
  * Simple implementation of a dns entry in the actor world.
  * @param reference the reference to the actor registered to the service.
  * @param name the name of the actor.
  * @param role the role of the actor into the system.
  * @param service the service offered by the actor.
  * @param used a boolean flag stating if the entry has been used recently.
  */
case class ActorDNSEntryImpl(override val reference: ActorRef, override val name: String, override val role: Role,
                             override val service: StringService, override var used: Boolean = false) extends ActorDNSEntry {

  override def equals(obj: Any): Boolean = obj match {
    case e: ActorDNSEntry => e.reference == reference && e.name == name && e.service == service
    case _ => false
  }

  override def ===(that: EntryParam): Boolean = role == that.role && service == that.service

  override def >(that: EntryParam): Boolean = role match {
    case INTERNAL_NODE if that.role != INTERNAL_NODE => service >= that.service
    case _ => service > that.service
  }

  override def >=(that: EntryParam): Boolean = this > that || this === that

  override def <(that: EntryParam): Boolean = that.role match {
    case INTERNAL_NODE if role != INTERNAL_NODE => service <= that.service
    case _ => service < that.service
  }

  override def <=(that: EntryParam): Boolean = this < that || this === that
}

object ActorDNSEntry {
  /**
    * Apply method to build an [[ActorDNSEntry]] object.
    * @param reference the actor reference.
    * @param name the actor name.
    * @param role the actor role into the system.
    * @param service the service offered by the actor.
    * @return a new instance of [[ActorDNSEntry]].
    */
  def apply(reference: ActorRef, name: String, role: Role, service: StringService): ActorDNSEntry =
    ActorDNSEntryImpl(reference = reference, name = name, role = role, service = service)
}