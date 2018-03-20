package org.gammf.proxima.dns

import play.api.libs.json._
import play.api.libs.functional.syntax._

package object dnsserver {
  /**
    * The application-level protocol used by this server. Could be http or https.
    */
  val APPLICATION_PROTOCOL: String = "http"

  /**
    * The local IP of the server, presumably the IP of the machine that contains the system.
    */
  val IP_ADDRESS: String = "localhost"

  /**
    * The server port.
    */
  val PORT_NUMBER: Int = 1406

  /**
    * The path to be used in order to contact the server about a Domain Name System issue.
    */
  val DNS_PATH: String = "dns"

  /**
    * The path to be used in order to contact the server about an address issue.
    */
  val ADDRESS_PATH: String = "address"

  /**
    * The complete URL to be used to contact the server about a DNS issue.
    */
  val COMPLETE_URL: String = APPLICATION_PROTOCOL + "://" + IP_ADDRESS + ":" + PORT_NUMBER + "/" + DNS_PATH + "/" + ADDRESS_PATH + "/"

  implicit val address2Json: Writes[(String, Int)] = Writes { address =>
    Json.obj(
      "ip" -> address._1,
      "port" -> address._2
    )
  }

  implicit val json2Info: Reads[(String, String, Int)] = (
    (JsPath \ "service").read[String] and
      (JsPath \ "ip").read[String] and
      (JsPath \ "port").read[Int]
  ) ((a: String, b: String, c: Int) => (a, b, c)) _
}