import { IWorkShift } from "./IWorkShift";

/**
 * Simple implementation of IWorkShift interface.
 */
export class WorkShift implements IWorkShift {

  /**
   * Creates a daily work shift, that begins in the given minute, and end in the given end minute.
   * Note that begin have to be minor of end.
   * @param begin the minute of day, in which the turn begins.
   * @param end the minute of day, in witch the turn ends.
   */
  public constructor(public begin: number, public end: number) { }

  public isInside(time: number) {
    return this.begin <= time && this.end > time;
  }

}