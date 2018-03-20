import * as crypto from "crypto";
import { PublicKeyQueries } from "./publicKeyQueries";

export class AuthorizationChecker {
  
  public static verifyDigitalSignature(operatorID: string, targetID: string, digitalSignature:string ): Promise<boolean> {

    return new Promise((resolve, reject) => {
      PublicKeyQueries.getPublicKey(operatorID).then((key) => {
        let verifier = crypto.createVerify('sha256');
        verifier.update(targetID);
        let verification =  verifier.verify(key, digitalSignature, "base64");
        if (verification) {
          resolve(true);
        }
        else {
          reject();
        }
      }).catch((err) => reject(err));
    })
    

    //return Promise.resolve(true); // mock object, always return true for now.
  }

  
}