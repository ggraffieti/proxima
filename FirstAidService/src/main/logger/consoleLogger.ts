import { AbstractConsoleLogger } from "./abstractConsoleLogger";
 
/**
 * A logger that log in the console.
 */
export class ConsoleLogger extends AbstractConsoleLogger { 

  /**
   * Build a new ConsoleLogger, that log data in the console.
   */
  public constructor() {
    super();
   }

  public logInConsole(loggingString: string) {
    this.log(this.formatConsoleLog(loggingString));
  }


  private log(loggingString: string) {
    console.log(loggingString);
  }

}