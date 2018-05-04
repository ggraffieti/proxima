import { ILogger } from "./ILogger";

/**
 * An abstract class that implements some utility methods for every logger needed in the system.
 */
export abstract class AbstractLogger implements ILogger {
  public abstract logDataAccess(rescuerID: string, patientID: string);
  public abstract logDataAccessDenied(rescuerID: string, patientID: string); 

  /**
   * Format the log string in a standard manner.
   * @param rescuerID the id of the rescuer.
   * @param patientID the id of the patient.
   */
  protected formatLog(rescuerID: string, patientID: string): string {
    return "[" + new Date() + "] rescuer: " + rescuerID + " acceded patient " + patientID + " medical data";
  } 
}