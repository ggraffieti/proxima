import * as mongoose from "mongoose";
import { keychain, IKeyModel } from "./publicKeyModel";

/**
 * Query class that hadles queries about public key authorization system.
 * Every method of this class returns a promise, wrapping the query result.
 * 
 * This class is static, it doesn't have state and it cannot be istantiate.
 */
export class PublicKeyQueries {

  // static non-instantiable class.
  private constructor() { }

  /**
   * Returns the public key of the given rescuer, if any, otherwise reject the promise with 
   * an Error.
   * @param rescuerId the id of the rescuer.
   */
  public static getPublicKey(rescuerId: string): Promise<string> {
    return new Promise((resolve, reject) => {
      keychain.findOne({rescuerID: rescuerId}).exec((err, doc: IKeyModel) => {
        if (err || !doc) {
          reject(err);
        }
        else {
          resolve(doc.getPublicKey());
        }
      });
    });  
  }

  /**
   * Adds a public key, and associates it to the given rescuer. This method throws an error (reject 
   * the promise with an error) if the rescuer is already present in the DB with an associated key. 
   * If you want to substitute the key of a rescure, please, use substitutePublicKey instead.
   * @param rescuerId the id of the rescuer.
   * @param key the public key, in PEM format.
   */
  public static addPublicKey(rescuerId: string, key: string): Promise<boolean> {
    return new Promise((resolve, reject) => {
      keychain.findOne({rescuerID: rescuerId}).exec((err, doc) => {
        if (err) {
          reject("Internal Error");
        }
        else if (doc) {
          reject("Key already present");
        }
        else {
          keychain.create({
            rescuerID: rescuerId,
            publicKey: key
          }, (err, _) => {
            if (err) {
              reject("Internal error");
            }
            else {
              resolve(true);
            }
          })
        }
      });
    });
  }

  /**
   * Substitute the public key of the given rescuer with the given key. This method throws an error if the rescuer is not present, or no key is asociated with him. If you want to add a new key to a new rescuer use addPublicKey instead.
   * @param rescuerId the id of the rescuer.
   * @param key the public key, in PEM format.
   */
  public static substitutePublicKey(rescuerId: string, key: string): Promise<boolean> {
    return new Promise((resolve, reject) => {
      keychain.findOneAndUpdate({rescuerID: rescuerId}, {publicKey: key}).exec((err, doc) => {
        if (err || !doc) {
          reject("Internal Error");
        }
        else {
          resolve(true);
        }
      });
    });
  }

  /**
   * Remove the association between the given rescuer and the associated public key.
   * If no key is associated with the given rescuer, or the rescuer not exists, nothing happens. 
   * @param rescuerId The id of the rescuer.
   */
  public static deletePublicKey(rescuerId: string): Promise<boolean> {
    return new Promise((resolve, reject) => {
      keychain.deleteOne({rescuerID: rescuerId}).exec((err, _ ) => {
        if (err) {
          reject("Internal error");
        }
        else {
          resolve(true);
        }
      });
    });
  }

}