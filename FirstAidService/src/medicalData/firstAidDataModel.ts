import * as mongoose from "mongoose";
import { IMedicalData } from "./utils/IMedicalData";
import { MedicalData } from "./utils/medicalData"
import { DatabaseAccess } from "../databaseAccess";

export interface IMedicalDataModel extends IMedicalData, mongoose.Document { 
  toMedicalData(): IMedicalData;
}

const medicalDataSchema = new mongoose.Schema({
  patientID: String,
  name: String,
  surname: String,
  CF: String,
  birthDate: Date,
  bloodGroup: String,
  organDonor: String,
  medicalConditions: [String],
  drugAllergies: [String],
  otherAllergies: [String],
  medications: [String]
});
medicalDataSchema.methods.toMedicalData = function(): IMedicalData {
  return new MedicalData(this.name, this.surname, this.CF, this.birthDate, this.bloodGroup, this.organDonor, this.medicalConditions, this.drugAllergies, this.otherAllergies, this.medications);
}

export const medicalData: mongoose.Model<IMedicalDataModel> = DatabaseAccess.getInstance().getMedicalDataConnection().model('medicalrecords', medicalDataSchema);