import { IWeeklyWorkShift } from "./IWeeklyWorkShift";
import { IWorkShift } from "./IWorkShift";

export class WeeklyWorkShift implements IWeeklyWorkShift {

  public constructor(private rescuer: string, private monday: IWorkShift[] = [], private tuesday: IWorkShift[] = [], private wednesday: IWorkShift[] = [], private thursday: IWorkShift[] = [], private friday: IWorkShift[] = [], private saturday: IWorkShift[] = [], private sunday: IWorkShift[] = []) {
  }

  public getRescuer(): string {
    return this.rescuer;
  }

  public checkShift(dayOfWeek: number, time: number) {
    console.log("check shift right now!");
    console.log(this);
    var daily = this.getDailyWorkShift(dayOfWeek);
    console.log("daily WS: " + daily);
    daily.forEach((e) => console.log(e.isInside(time)));
    return daily.some((shift: IWorkShift) => shift.isInside(time));
  }

  private getDailyWorkShift(dayOfWeek: number): IWorkShift[] {
    switch (dayOfWeek) {
      case 1: return this.monday;
      case 2: return this.tuesday;
      case 3: return this.wednesday;
      case 4: return this.thursday;
      case 5: return this.friday;
      case 6: return this.saturday;
      case 7: return this.sunday;
      default: return [];
    }
  }

}