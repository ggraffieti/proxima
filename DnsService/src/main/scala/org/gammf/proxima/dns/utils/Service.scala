package org.gammf.proxima.dns.utils

import scala.annotation.tailrec

/**
  * Represents a generic service, structured as domain names, in order to identify the address of a specific service.
  * Every service consists of a main domain and can contain some other subdomains.
  * @tparam A the generic domain name type class.
  */
sealed trait Service[A] {
  /**
    * Returns the main domain.
    */
  def main: Option[A]

  /**
    * Returns the subdomains.
    */
  def subdomains: Option[Service[A]]

  /**
    * Returns the last domain.
    */
  def last: Option[A]

  /**
    * Returns the size of the service, as the number of structured domains.
    */
  def size(): Int

  /**
    * Returns a [[Service]] containing the elements between the specified indexes.
    * @param minIndex the lower bound index.
    * @param maxIndex the upper bound index.
    */
  def getInBetween(minIndex: Int, maxIndex: Int): Service[A]

  /**
    * Greater method. Checks if this service is greater than some other service.
    * @param that the other service to be compared to this service.
    * @return true if this service contains more domains than the given service, false otherwise.
    */
  def >(that: Service[A]): Boolean

  /**
    * Greater or equals method. Checks if this service is equals or greater than some other service.
    * @param that the other service to be compared to this service.
    * @return true if this service contains more or equal domains than the given service, false otherwise.
    */
  def >=(that: Service[A]): Boolean

  /**
    * Lesser method. Checks if this service is lesser than some other service.
    * @param that the other service to be compared to this service.
    * @return true if this service contains less domains than the given service, false otherwise.
    */
  def <(that: Service[A]): Boolean
  /**
    * Lesser or equals method. Checks if this service is equals or lesser than some other service.
    * @param that the other service to be compared to this service.
    * @return true if this service contains less or equal domains than the given service, false otherwise.
    */
  def <=(that: Service[A]): Boolean

  /**
    * A copy of this service with an element appended as subdomain.
    * @param element the element to be appended.
    * @return a new service of all elements of this service followed by element as subdomain.
    */
  def :+(element: A): Service[A]

  /**
    * A copy of this service with an element prepended as main domain.
    * @param element the element to be prepended.
    * @return a new service consisting of the element as main domain followed by all elements of this service.
    */
  def +:(element: A): Service[A]

  /**
    * Returns a new service containing the elements from this service followed by the elements from some other service.
    * @param that the service to be appended.
    * @return a new service which contains all elements of this service followed by all elements of that.
    */
  def ++(that: Service[A]): Service[A]

  /**
    * Adds an element as main domain of this service.
    * @param element the element to be prepended.
    * @return a service which contains the element as main domain and which continues with this service domains.
    */
  def ::(element: A): Service[A]
}

/**
  * Represents an actual service containing at least a main domain.
  * @param _main the main domain.
  * @param _subdomains the subdomains.
  * @tparam A the generic service type class.
  */
case class ActualService[A](_main: A, _subdomains: Service[A]) extends ServiceImpl[A]

/**
  * Represents an empty service.
  * @tparam A the generic service type class.
  */
case class EmptyService[A]() extends ServiceImpl[A]

object :: {
  def unapply[A](service: Service[A]): Option[(A, Service[A])] = service match {
    case ActualService(m, s) => Some((m, s))
    case _ => None
  }
}

/**
  * A generic implementation of a structured service.
  * @tparam A the generic service type class.
  */
trait ServiceImpl[A] extends Service[A] {

  override def main : Option[A] = this match {
    case m ::_ => Some(m)
    case _ => None
  }

  override def subdomains: Option[Service[A]] = this match {
    case _ :: s => Some(s)
    case _ => None
  }

  override def last: Option[A] = {
    @tailrec
    def getLast(service: Service[A]): Option[A] = service match {
      case m :: EmptyService() => Some(m)
      case _ :: s => getLast(s)
      case _ => None
    }
    getLast(this)
  }

  override def size(): Int = {
    @tailrec
    def getSize(service: Service[A], size: Int): Int = service match {
      case _ :: s => getSize(s, size + 1)
      case _ => size
    }
    getSize(this, 0)
  }

  override def getInBetween(minIndex: Int, maxIndex: Int): Service[A] = {
    def getElems(l: Service[A], i: Int, res: Service[A]): Service[A] = l match {
      case _ :: s if i < minIndex => getElems(s, i+1, res)
      case m :: s if i <= maxIndex => getElems(s, i+1, res :+ m)
      case _ => res
    }
    getElems(this, 0, Service())
  }

  override def equals(obj: Any): Boolean = obj match {
    case service: Service[A] => this match {
      case _ if size != service.size => false
      case m :: s => m == service.main.get && s == service.subdomains.get
      case _ => true
    }
    case _ => false
  }

  override def >(service: Service[A]): Boolean = this match {
    case _ if size >= service.size => false
    case m :: s => m == service.main.get && s > service.subdomains.get
    case _ => true
  }

  override def >=(service: Service[A]): Boolean = this > service || this == service

  override def <(service: Service[A]): Boolean = service > this

  override def <=(service: Service[A]): Boolean = service >= this

  override def :+(element: A): Service[A] = this match {
    case m :: s => m :: (s :+ element)
    case _ => element :: EmptyService()
  }

  override def +:(element: A): Service[A] = element :: this

  override def ++(service: Service[A]): Service[A] = this match {
    case m :: s => m :: (s ++ service)
    case _ => service
  }

  override def ::(m: A): Service[A] = ActualService(m, this)

  override def toString: String = this match {
    case m :: s if s != EmptyService() => m + "." + s.toString
    case m :: _ => m + ""
  }
}

object Service {
  /**
    * Apply method to build a [[Service]] object.
    * @param domains the domains of the service.
    * @tparam A the generic type of the domains of the service.
    * @return an object of type [[Service]].
    */
  def apply[A](domains :A*): Service[A] = {
    var service: Service[A] = EmptyService()
    for (i <- domains.length -1 to 0 by -1) service = domains(i) :: service
    service
  }

  /**
    * The type of a Service in which the domains are represented as strings.
    */
  type StringService = Service[String]
}

/**
  * Represents the address of a component offering a service.
  * @param ip the IP address of the component.
  * @param port the port number of the component.
  */
case class ServiceAddress(ip: String, port: Int) {
  override def toString: String = ip + ":" + port
}