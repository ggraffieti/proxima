import { IWorkShift } from "./IWorkShift";

export class WorkShift implements IWorkShift {

  public constructor(public begin: number, public end: number) { }

  public isInside(time: number) {
    return this.begin <= time && this.end > time;
  }

}