import * as mongoose from "mongoose";
import { IWeeklyWorkShift } from "./utils/IWeeklyWorkShift";
import { WeeklyWorkShift } from "./utils/weeklyWorkShift";
import { DatabaseAccess } from "../databaseAccess";
import { WorkShift } from "./utils/workShift";

/**
 * Interface that maps the mongoose model to an object oriented interface. 
 */
export interface IWeeklyWorkShiftModel extends mongoose.Document { 
  /**
   * returns the IWeeklyWorkShift contained in a mongoose document.
   */
  getWeeklyWorkShift(): IWeeklyWorkShift;
}

/**
 * The schema of a single shift DB entry, used by mongoose for validate queries.
 */
const shiftSchema = new mongoose.Schema({
  begin: Number,
  end: Number
}, {_id: false});

/**
 * The schema of the weekly work shift DB entry, used by mongoose for validate queries.
 */
const workShiftsSchema = new mongoose.Schema({
  rescuerID: String,
  monday: [shiftSchema],
  tuesday: [shiftSchema],
  wednesday: [shiftSchema],
  thursday: [shiftSchema],
  friday: [shiftSchema],
  saturday: [shiftSchema],
  sunday: [shiftSchema]
});
workShiftsSchema.methods.getWeeklyWorkShift = function(): IWeeklyWorkShift {
  return new WeeklyWorkShift(this.rescuerID, this.monday.map((w) => new WorkShift(w.begin, w.end)), this.tuesday.map((w) => new WorkShift(w.begin, w.end)), this.wednesday.map((w) => new WorkShift(w.begin, w.end)), this.thursday.map((w) => new WorkShift(w.begin, w.end)), this.friday.map((w) => new WorkShift(w.begin, w.end)), this.saturday.map((w) => new WorkShift(w.begin, w.end)), this.sunday.map((w) => new WorkShift(w.begin, w.end)));
}

/**
 * The model of the work shift collection.
 */
export const workShifts: mongoose.Model<IWeeklyWorkShiftModel> = DatabaseAccess.getInstance().getMedicalDataConnection().model('rescuersworkschedules', workShiftsSchema);