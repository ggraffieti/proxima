import * as mongoose from "mongoose";
import { IWeeklyWorkShiftModel, workShifts } from "./workShiftsModel";
import { DatabaseError } from "../errors/databaseError";

/**
 * Query class that handles queries about rescuers work shifts.
 * Every method of this class returns a promise, wrapping the query result.
 * 
 * This class is static, it doesn't have state and it cannot be istantiate.
 */
export class WorkShiftQueries {

  /**
   * Returns a promise, wrapping the result of a query, checking if the given rescuer has a valid work
   * shift when performs a request. If a rescuer is outside work time, the promise is rejected. If 
   * the rescuers is working when the request is performed, the promise is resolved, with boolean
   * true value.
   * @param rescuerId the id of the rescuer that performs the request.
   */
  public static rescuerAuthorization(rescuerId: string): Promise<boolean> {
    return new Promise((resolve, reject) => {
      workShifts.findOne({rescuerID: rescuerId}).exec((err, doc: IWeeklyWorkShiftModel) => {
        if (err || !doc) {
          reject(new DatabaseError());
        }
        else {
          let date: Date = new Date();
          resolve(doc.getWeeklyWorkShift().checkShift(date.getDay(), (date.getHours() * 60) + date.getMinutes()));
        }
      });
    });
  }

}