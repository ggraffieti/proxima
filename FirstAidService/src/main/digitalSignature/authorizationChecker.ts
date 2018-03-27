import * as crypto from "crypto";
import { PublicKeyQueries } from "./publicKeyQueries";

export class AuthorizationChecker {
  
  public static verifyDigitalSignature(operatorID: string, targetID: string, digitalSignature:string ): Promise<boolean> {
    console.log("Verify digital signature");
    console.log(digitalSignature);
    return new Promise((resolve, reject) => {
      PublicKeyQueries.getPublicKey(operatorID).then((key) => {
        let verifier = crypto.createVerify('RSA-SHA256');
        verifier.update(targetID + operatorID);
        let verification =  verifier.verify(key, digitalSignature, "base64");
        if (verification) {
          resolve(true);
        }
        else {
          reject("Wrong digital signature");
        }
      }).catch((_) => reject("Canno get key of operator " + operatorID));
    })
    

    //return Promise.resolve(true); // mock object, always return true for now.
  }

  
}