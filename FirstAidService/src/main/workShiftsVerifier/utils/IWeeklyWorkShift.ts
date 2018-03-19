export interface IWeeklyWorkShift {
    getRescuer(): string;
    /**
     * Checks if this worker has a shift in the given day of the week, and in the given time.
     * @param dayOfWeek The day of the week (0-6) 0 is monday, 6 is sunday.
     * @param time the time of the day, in minutes after midnight (e.g. 5:24 AM = 324 (60*5+24)). 
     */
    checkShift(dayOfWeek: number, time: number): boolean;
}