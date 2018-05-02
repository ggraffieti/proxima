import { expect } from 'chai';
import 'mocha';
import  { IWeeklyWorkShift } from "../../main/workShiftsVerifier/utils/IWeeklyWorkShift";
import  { WeeklyWorkShift } from "../../main/workShiftsVerifier/utils/weeklyWorkShift";
import { WorkShift } from '../../main/workShiftsVerifier/utils/workShift';


var weeklyWorkShift: IWeeklyWorkShift;

describe('Weekly work shift', () => {

  before(() => {
    weeklyWorkShift = new WeeklyWorkShift("rescuer1", [new WorkShift(0, 480)], [new WorkShift(0, 480)], [new WorkShift(0, 480)], [new WorkShift(480, 960)], [new WorkShift(960, 1440)], undefined, undefined);
  });

  it('should correctly return the correct rescuer', () => {
    expect(weeklyWorkShift.getRescuer()).to.equal("rescuer1");
  });

  it('should return true if the rescuer is working on the given time', () => {
    expect(weeklyWorkShift.checkShift(1, 300)).to.equal(true);
    expect(weeklyWorkShift.checkShift(2, 1)).to.equal(true);
    expect(weeklyWorkShift.checkShift(3, 450)).to.equal(true);
    expect(weeklyWorkShift.checkShift(4, 550)).to.equal(true);
    expect(weeklyWorkShift.checkShift(5, 1439)).to.equal(true);
  });

  it('should return false if the rescuer is not working on the given time', () => {
    expect(weeklyWorkShift.checkShift(1, 1140)).to.equal(false);
    expect(weeklyWorkShift.checkShift(2, 480)).to.equal(false);
    expect(weeklyWorkShift.checkShift(5, 450)).to.equal(false);
    expect(weeklyWorkShift.checkShift(6, 0)).to.equal(false);
    expect(weeklyWorkShift.checkShift(7, 540)).to.equal(false);
  });

});
