package org.gammf.proxima.dns

import akka.actor.ActorRef
import org.gammf.proxima.dns.utils.Service.StringService
import org.gammf.proxima.dns.utils.{Service, ServiceAddress}

object TestsUtils {
  val STRING_ROOT_SERVICE: String = "proxima"
  val ROOT_SERVICE: StringService = Service() :+ STRING_ROOT_SERVICE

  val MEDICAL_NAME: String = "medical"
  val STRING_MEDICAL_SERVICE: String = STRING_ROOT_SERVICE + "." + MEDICAL_NAME
  val MEDICAL_SERVICE: StringService = ROOT_SERVICE :+ MEDICAL_NAME

  val EXAMPLE_REFERENCE: ActorRef = ActorRef.noSender
  val EXAMPLE_NAME: String = "example"
  val STRING_EXAMPLE_SERVICE: String = STRING_ROOT_SERVICE + "." + EXAMPLE_NAME
  val EXAMPLE_SERVICE: StringService = ROOT_SERVICE :+ EXAMPLE_NAME
  val EXAMPLE_IP: String = "example.com"
  val EXAMPLE_PORT: Int = 1111
  val EXAMPLE_ADDRESS: ServiceAddress = ServiceAddress(ip = EXAMPLE_IP, port = EXAMPLE_PORT)

  val FIRST_AID_NAME: String = "firstAid"
  val STRING_FIRST_AID_SERVICE: String = STRING_MEDICAL_SERVICE + "." + FIRST_AID_NAME
  val FIRST_AID_SERVICE: StringService = MEDICAL_SERVICE :+ "firstAid"
  val FIRST_AID_IP: String = "192.168.0.1"
  val FIRST_AID_PORT: Int = 1406
  val FIRST_AID_ADDRESS: ServiceAddress = ServiceAddress(ip = FIRST_AID_IP, port = FIRST_AID_PORT)

  val EXAM_NAME: String = "exam"
  val STRING_EXAM_SERVICE: String = STRING_MEDICAL_SERVICE + "." + EXAM_NAME
  val EXAM_SERVICE: StringService = MEDICAL_SERVICE :+ EXAM_NAME
  val EXAM_IP: String = "192.168.0.2"
  val EXAM_PORT: Int = 1406
  val EXAM_ADDRESS: ServiceAddress = ServiceAddress(ip = EXAM_IP, port = EXAM_PORT)
}
