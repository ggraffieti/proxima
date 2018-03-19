import { ILogger } from "./ILogger";

export abstract class AbstractLogger implements ILogger {
  public abstract logDataAccess(rescuerID: string, patientID: string);
  public abstract logDataAccessDenied(rescuerID: string, patientID: string); 

  protected formatLog(rescuerID: string, patientID: string) {
    return "[" + new Date() + "] rescuer: " + rescuerID + " acceded patient " + patientID + " medical data";
  } 
}