package org.gammf.proxima.dns.general

import akka.util.Timeout
import scala.concurrent.duration._
import scala.language.postfixOps

package object actors {
  /**
    * An implicit that sets the default timeout of an ask pattern operation.
    */
  implicit val timeout: Timeout = Timeout(5 seconds)
}
