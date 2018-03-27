import { NetworkManager } from "./networkManager";

export class DnsRegistration {
  
  private constructor() { }

  public static register() {
    // POST -- /dns/address 
/*
    {
      "service": "proxima.medica.firstAid",
      "ip": "192.168.1.25",
      "port": "9876"
    }
    */
   // response OK -> 201 !
   // porta dns 1406.

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