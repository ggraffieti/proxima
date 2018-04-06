import { NetworkManager } from "./networkManager";
import { FileServerConfiguration } from "../configuration/fileServerConfiguration"

/**
 * A static, non instantiable class that manages DNS registration. 
 * The DNS is a core part of the proxima system, and every service have to be registered on it.
 * If the service is not registered in the DNS no one can contact it. 
 */
export class DnsRegistration {

  private static protocol = "http://";
  private static dnsPath = "/dns/address";
  private static serviceOffered = "proxima.medical.firstAid";
  
  private constructor() { }

  /**
   * Register the service on the DNS.
   * If an error occurred (no network, or dns server down) this method retries the registration after
   * one second.
   */
  public static register() {
   NetworkManager.sendHttpPost(this.protocol + FileServerConfiguration.getInstance().dnsIp + ":" + FileServerConfiguration.getInstance().dnsPort + this.dnsPath, {
     service: this.serviceOffered,
     ip: FileServerConfiguration.getInstance().localIp,
     port: FileServerConfiguration.getInstance().localPort
   }, (error, response, _) => {
     if (error || response.statusCode != 201) {
       this.retryDnsRegistration(() => this.register()); // retry registration after 1 second
       console.log("error on DNS registration");
     }
     else {
       console.log("service correctly registered on proxima DNS");
     }
   })
  }

  private static retryDnsRegistration(dnsRegistrationFunction: () => void) {
    setTimeout(dnsRegistrationFunction, 1000);
  }

}