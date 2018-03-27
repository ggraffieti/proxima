import { NetworkManager } from "./networkManager";

export class DnsRegistration {
  
  private constructor() { }

  public static register() {
   NetworkManager.sendHttpPost("http://192.168.43.57:1406/dns/address", {
     service: "proxima.medical.firstAid",
     ip: "192.168.43.231",
     port: 9876
   }, (error, response, _) => {
     if (error || response.statusCode != 201) {
       console.log("error on DNS registration");
     }
     else {
       console.log("service correctly registered on proxima DNS");
     }
   })
  }

}