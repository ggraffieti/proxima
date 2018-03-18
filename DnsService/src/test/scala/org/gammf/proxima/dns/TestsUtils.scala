package org.gammf.proxima.dns

import akka.actor.ActorRef
import org.gammf.proxima.dns.utils.Service.StringService
import org.gammf.proxima.dns.utils.{Service, ServiceAddress}

object TestsUtils {
  val ROOT_SERVICE: StringService = Service() :+ "proxima"
  val MEDICAL_SERVICE: StringService = ROOT_SERVICE :+ "medical"

  val EXAMPLE_REFERENCE: ActorRef = ActorRef.noSender
  val EXAMPLE_NAME: String = "Example"
  val EXAMPLE_SERVICE: StringService = ROOT_SERVICE :+ "example"
  val EXAMPLE_ADDRESS: ServiceAddress = ServiceAddress(ip = "example.com", port = 1111)

  val FIRST_AID_NAME: String = "FirstAid"
  val FIRST_AID_SERVICE: StringService = MEDICAL_SERVICE :+ "firstAid"
  val FIRST_AID_ADDRESS: ServiceAddress = ServiceAddress(ip = "192.168.0.1", port = 1406)

  val EXAM_NAME: String = "Exam"
  val EXAM_SERVICE: StringService = MEDICAL_SERVICE :+ "exam"
  val EXAM_ADDRESS: ServiceAddress = ServiceAddress(ip = "192.168.0.2", port = 1406)
}
