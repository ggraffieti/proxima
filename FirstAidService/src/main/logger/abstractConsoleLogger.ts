import { AbstractLogger } from "./abstractLogger";

/**
 * An abstract class that implements some utility methods for every console logger needed in the system.
 */
export abstract class AbstractConsoleLogger extends AbstractLogger {
  
  /**
   * A method that prints the given string in the console.
   * @param loggingString the string to log in the console.
   */
  public abstract logInConsole(loggingString: string);

  public logDataAccess(rescuerID: string, patientID: string) {
    this.logInConsole(this.formatLog(rescuerID, patientID));
  }

  public logDataAccessDenied(rescuerID: string, patientID: string) {
    this.logInConsole(this.formatLog(rescuerID, patientID));
  }
  

  /**
   * Format the log string in a standard manner.
   * @param loggingString the string to format, for console logging.
   */
  protected formatConsoleLog(loggingString: string) {
    return "[" + new Date() + "] " + loggingString;
  } 

  protected formatLog(rescuerID: string, patientID: string): string {
    return this.formatConsoleLog("rescuer: " + rescuerID + " acceded patient " + patientID + " medical data");
  }

}