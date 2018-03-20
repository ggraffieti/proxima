import * as mongoose from "mongoose";
import { keychain, IKeyModel } from "./publicKeyModel";

export class PublicKeyQueries {

  // static non-instantiable class.
  private constructor() { }

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