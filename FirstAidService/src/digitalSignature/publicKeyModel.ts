import * as mongoose from "mongoose";
import { DatabaseAccess } from "../databaseAccess";

export interface IKeyModel extends mongoose.Document {
  getPublicKey(): string;
}

const keySchema = new mongoose.Schema({
  rescuerID: String,
  publicKey: String
});
keySchema.methods.getPublicKey = function(): string {
  return this.publicKey;
}

export const keychain: mongoose.Model<IKeyModel> = DatabaseAccess.getInstance().getKeyDataConnection().model("keys", keySchema);