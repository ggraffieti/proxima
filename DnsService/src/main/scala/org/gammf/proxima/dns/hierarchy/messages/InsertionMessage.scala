package org.gammf.proxima.dns.hierarchy.messages

import akka.actor.ActorRef
import org.gammf.proxima.dns.utils.Role.Role
import org.gammf.proxima.dns.utils.Service.StringService

/**
  * Represents a request from an actor to a [[org.gammf.proxima.dns.hierarchy.actors.DNSRouterActor]] that leads to an
  * entry insertion in the DNS hierarchy.
  */
sealed trait InsertionRequestMessage extends DNSMessage {
  /**
    * Returns the reference to the actor that needs to be inserted.
    */
  def reference: ActorRef

  /**
    * Returns the name of the actor that needs to be inserted.
    */
  def name: String

  /**
    * Returns the role of the actor that needs to be inserted.
    */
  def role: Role

  /**
    * Returns the service offered by the actor.
    */
  def service: StringService
}

/**
  * Represents a registration request from an actor that wants to be included in the dns hierarchy.
  * @param reference the reference to the actor that sends the registration request.
  * @param name the name of the actor that sends the registration request.
  * @param role the role of the actor in the DNS hierarchy.
  * @param service the service offered by the actor.
  */
case class RegistrationRequestMessage(override val reference: ActorRef, override val name: String, override val role: Role,
                                      override val service: StringService) extends InsertionRequestMessage

/**
  * Represents a message between objects of type [[org.gammf.proxima.dns.hierarchy.actors.DNSRouterActor]] containing the info about an entry.
  * An actor should send this message to delegate an entry management to another actor.
  *
  * @param reference the reference of the actor of the entry.
  * @param name the name of the actor of the entry.
  * @param role the role of the actor of the entry.
  * @param service the service of the actor of the entry.
  */
case class RedirectionRequestMessage(override val reference: ActorRef, override val name: String, override val role: Role,
                                     override val service: StringService) extends InsertionRequestMessage

/**
  * It's a response to an [[InsertionRequestMessage]].
  */
sealed trait InsertionResponseMessage extends DNSMessage

/**
  * Represents a confirmation of actor registration in the DNS hierarchy.
  * It's a response to a [[RegistrationRequestMessage]].
  */
case class RegistrationResponseMessage() extends InsertionResponseMessage

/**
  * Represents a confirmation of actor redirection in the DNS hierarchy.
  * It's a response to a [[RedirectionRequestMessage]].
  *
  * @param reference the reference to the actor of the entry.
  * @param name the name of the actor of the entry.
  * @param role the role of the actor of the entry.
  * @param service the service of the entry.
  */
case class RedirectionResponseMessage(reference: ActorRef, name: String, role: Role, service: StringService)
  extends InsertionResponseMessage

/**
  * Represents an error happened during an actor insertion in the DNS hierarchy.
  * It's a negative response to a [[InsertionRequestMessage]].
  */
case class InsertionErrorMessage() extends InsertionResponseMessage