import { NetworkManager } from "./networkManager";
import { FileServerConfiguration } from "../configuration/fileServerConfiguration"

export class DnsRegistration {

  private static protocol = "http://";
  private static dnsPath = "dns/address";
  private static serviceOffered = "proxima.medica.firstAid";
  
  private constructor() { }

  public static register() {
   NetworkManager.sendHttpPost(this.protocol + FileServerConfiguration.getInstance().dnsIp + FileServerConfiguration.getInstance().dnsPort + this.dnsPath, {
     service: this.serviceOffered,
     ip: FileServerConfiguration.getInstance().localIp,
     port: FileServerConfiguration.getInstance().localPort
   }, (error, response, _) => {
     if (error || response.statusCode != 201) {
       this.retryDnsRegistration(); // retry registration after 1 second
       console.log("error on DNS registration");
     }
     else {
       console.log("service correctly registered on proxima DNS");
     }
   })
  }

  private static retryDnsRegistration() {
    setTimeout(DnsRegistration.register, 1000);
  }

}