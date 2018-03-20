export interface IWorkShift {
  begin: number;
  end: number;

  isInside(time: number): boolean;

}