import { expect } from 'chai';
import 'mocha';
import  { IWorkShift } from "../../main/workShiftsVerifier/utils/IWorkShift";
import { WorkShift } from '../../main/workShiftsVerifier/utils/workShift';

var workShift: IWorkShift;

describe('Work shift', () => {

  before(() => {
    workShift = new WorkShift(60, 440);
  });

  it('should be return true if the given time is inside the shift', () => {
    expect(workShift.isInside(60)).to.equal(true);
    expect(workShift.isInside(300)).to.equal(true);
    expect(workShift.isInside(439)).to.equal(true);
  });

  it('should be return false if the given time is not inside the shift', () => {
    expect(workShift.isInside(0)).to.equal(false);
    expect(workShift.isInside(59)).to.equal(false);
    expect(workShift.isInside(440)).to.equal(false);
    expect(workShift.isInside(560)).to.equal(false);
  });

});