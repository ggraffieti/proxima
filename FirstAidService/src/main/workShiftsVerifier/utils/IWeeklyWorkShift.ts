export interface IWeeklyWorkShift {

  /**
   * Returns the rescuer asociated with this work shift.
   */
  getRescuer(): string;
  /**
   * Checks if this worker has a work shift in the given day of the week, and in the given time.
   * @param dayOfWeek The day of the week (1-7) 1 is monday, 7 is sunday.
   * @param time the time of the day, in minutes after midnight (e.g. 5:24 AM = 324 (60*5+24)). 
   */
  checkShift(dayOfWeek: number, time: number): boolean;
}