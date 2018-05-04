import * as mongoose from "mongoose";
import { DatabaseAccess } from "../databaseAccess";

/**
 * Interface that maps the mongoose model to an object oriented interface. 
 */
export interface IKeyModel extends mongoose.Document {
  /**
   * Get the public key from a mongoose document.
   */
  getPublicKey(): string;
}

/**
 * The schema of the public key DB entry, used by mongoose for validate queries.
 */
const keySchema = new mongoose.Schema({
  rescuerID: String,
  publicKey: String
});
keySchema.methods.getPublicKey = function(): string {
  return this.publicKey;
}

/**
 * The model of the keys collection.
 */
export const keychain: mongoose.Model<IKeyModel> = DatabaseAccess.getInstance().getKeyDataConnection().model("keys", keySchema);