export interface IWorkShift {
  getStartTime(): number;
  getEndTime(): number;

  isInside(time: number): boolean;

}