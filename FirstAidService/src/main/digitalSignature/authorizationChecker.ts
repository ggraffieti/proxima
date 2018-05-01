import * as crypto from "crypto";
import { PublicKeyQueries } from "./publicKeyQueries";
import { DatabaseError } from "../errors/databaseError";
import { WrongDigitalSignatureError } from "../errors/wrongDigitalSignatureError";

/**
 * Static class that performs the verification of the digital signature. 
 * 
 * This class is static, it doesn't have state and it cannot be istantiate.
 */
export class AuthorizationChecker {

  private constructor() {}
  
  /**
   * Verifies the digial signature of the given operator, which asks data of the given target. 
   * This method is asynchronous, and returns a Promise wrapping the result. Only if the verification 
   * ends successfully the promise is resolved, otherwise is rejected.
   * @param operatorID the id of the operator that asks for data. 
   * @param targetID the id of the target.
   * @param digitalSignature the digital signature of the operator, contains an hash (sha 256) of the 
   * concatenation of targetID + operatorID, encrypted with the private key of the operator.
   */
  public static verifyDigitalSignature(operatorID: string, targetID: string, digitalSignature:string ): Promise<boolean> {
    return new Promise((resolve, reject) => {
      PublicKeyQueries.getPublicKey(operatorID).then((key) => {
        let verifier = crypto.createVerify('RSA-SHA256');
        verifier.update(targetID + operatorID);
        let verification =  verifier.verify(key, digitalSignature, "base64");
        if (verification) {
          resolve(true);
        }
        else {
          reject(new WrongDigitalSignatureError());
        }
      }).catch((_) => reject(new DatabaseError("Cannot get key of rescuer " + operatorID)));
    })
  }

}