package org.gammf.proxima

import akka.util.Timeout
import org.gammf.proxima.dns.utils.Service
import org.gammf.proxima.dns.utils.Service.StringService

import scala.concurrent.duration._
import scala.language.postfixOps

package object dns {
  /**
    * An implicit that sets the default timeout of an ask pattern operation.
    */
  implicit val timeout: Timeout = Timeout(5 seconds)

  /**
    * The name of the actor system for the Proxima DNS service.
    */
  val ACTOR_SYSTEM_NAME: String = "ProximaDNS"

  /**
    * The main domain of the Proxima DNS service.
    */
  val DNS_ROOT_SERVICE: StringService = Service() :+ "proxima"

  /**
    * The time to wait before the first hierarchy control takes place.
    */
  val CONTROL_INITIAL_DELAY: FiniteDuration = 1 second

  /**
    * The elapsed time between two consecutive hierarchy controls.
    */
  val CONTROL_INTERVAL: FiniteDuration = 10 seconds

  /**
    * The threshold of the hierarchy controls.
    */
  val CONTROL_THRESHOLD: Int = 3

  /**
    * The time to wait before the first hierarchy print takes place.
    */
  val PRINTING_INITIAL_DELAY: FiniteDuration = 1 second

  /**
    * The elapsed time between two consecutive hierarchy prints.
    */
  val PRINTING_INTERVAL: FiniteDuration = 20 seconds
}
