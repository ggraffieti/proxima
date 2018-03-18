import * as mongoose from "mongoose";
import { IWeeklyWorkShiftModel, workShifts } from "./workShiftsModel";

export class WorkShiftQueries {

  public static rescuerAuthorization(rescuerId: string): Promise<boolean> {
    console.log("RID: " + rescuerId);
    return new Promise((resolve, reject) => {
      workShifts.findOne({rescuerID: rescuerId}).exec((err, doc: IWeeklyWorkShiftModel) => {
        if (err || !doc) {
          reject(err);
        }
        else {
          let date: Date = new Date();
          resolve(doc.getWeeklyWorkShift().checkShift(date.getDay(), (date.getHours() * 60) + date.getMinutes()));
        }
      });
    });
  }

}