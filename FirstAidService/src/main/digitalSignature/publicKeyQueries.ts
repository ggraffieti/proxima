import * as mongoose from "mongoose";
import { keychain, IKeyModel } from "./publicKeyModel";

export class PublicKeyQueries {

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

}