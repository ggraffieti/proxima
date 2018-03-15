import { IWorkShift } from "./IWorkShift";

export class WorkShift implements IWorkShift {

  public constructor(private startTime: number, private endTime: number) { }

  public getStartTime() {
    return this.startTime;
  }

  public getEndTime() {
    return this.endTime;
  }

  public isInside(time: number) {
    return this.getStartTime() <= time && this.getEndTime() > time;
  }

}