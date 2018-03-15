import * as mongoose from "mongoose";
import { IWeeklyWorkShift } from "./utils/IWeeklyWorkShift";
import { WeeklyWorkShift } from "./utils/WeeklyWorkShift";

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
  return new WeeklyWorkShift(this.rescuerID, this.monday, this.tuesday, this.wednesday, this.thursday, this.friday, this.saturday, this.sunday);
}

export const workShifts: mongoose.Model<IWeeklyWorkShiftModel> = mongoose.model('rescuersWorkSchedule', workShiftsSchema);