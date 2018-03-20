import * as mongoose from "mongoose";
import { IMedicalData } from "./utils/IMedicalData";
import { medicalData, IMedicalDataModel } from "./firstAidDataModel"

export class MedicalDataQueries {

  public static getPatientData(patientId: string): Promise<IMedicalData> {

    return new Promise((resolve, reject) => {
      medicalData.findOne({patientID: patientId}).exec((err, doc: IMedicalDataModel) => {
        if (err || !doc) {
          reject(err);
        }
        else {
          resolve(doc.toMedicalData());
        }
      });
    });

  }
}