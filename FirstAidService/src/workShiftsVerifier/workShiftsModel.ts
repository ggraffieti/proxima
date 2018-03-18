import * as mongoose from "mongoose";
import { IWeeklyWorkShift } from "./utils/IWeeklyWorkShift";
import { WeeklyWorkShift } from "./utils/weeklyWorkShift";
import { DatabaseAccess } from "../databaseAccess";
import { WorkShift } from "./utils/workShift";

export interface IWeeklyWorkShiftModel extends mongoose.Document { 
  getWeeklyWorkShift(): IWeeklyWorkShift;
}

const shiftSchema = new mongoose.Schema({
  begin: Number,
  end: Number
}, {_id: false});

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

export const workShifts: mongoose.Model<IWeeklyWorkShiftModel> = DatabaseAccess.getInstance().getMedicalDataConnection().model('rescuersworkschedules', workShiftsSchema);