/**
 * Represents a single day work shift. It begins at a certain minute of the day, and finish 
 * at certain minute of the same day. 
 */
export interface IWorkShift {
  /**
   * The minute of day when the work shift begins.
   * E.g.
   * if the turn begins at 9.30 AM the begin, in minutes, is 570 (9*60)+30.
   */
  begin: number;
  /**
   * The minute of the day when the work shift ends.
   * E.g.
   * if the turn ends at 19.45 (7.45 PM) the end, in minutes, is 1185 (19*60)+45.
   */
  end: number;

  /**
   * Returns true if the given minute is inside this work shift, false otherwise. 
   * @param time the minute of the day to check.
   */
  isInside(time: number): boolean;

}