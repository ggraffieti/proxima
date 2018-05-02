import * as mongoose from "mongoose";
import { IMedicalData } from "./utils/IMedicalData";
import { medicalData, IMedicalDataModel } from "./firstAidDataModel"
import { DatabaseError } from "../errors/databaseError";

/**
 * Query class that hadles queries about first aid medical data.
 * Every method of this class returns a promise, wrapping the query result.
 * 
 * This class is static, it doesn't have state and it cannot be istantiate.
 */
export class MedicalDataQueries {

  /**
   * Returns medical data of the given patient, if any, otherwise reject the promise with 
   * an Error.
   * @param rescuerId the id of the patient.
   */
  public static getPatientData(patientId: string): Promise<IMedicalData> {
    return new Promise((resolve, reject) => {
      medicalData.findOne({patientID: patientId}).exec((err, doc: IMedicalDataModel) => {
        if (err || !doc) {
          reject(new DatabaseError());
        }
        else {
          resolve(doc.toMedicalData());
        }
      });
    });
  }
  
}