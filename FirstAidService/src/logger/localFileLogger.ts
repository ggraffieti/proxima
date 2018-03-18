import * as fs from "fs";
import { ILogger } from "./ILogger";

export class LocalFileLogger implements ILogger {

  private static SINGLETON = new LocalFileLogger();
  private static dataAccessFile = "/log/dataAccess.txt";
  private static dataAccessDeniedFile = "/log/dataAccessDenied.txt";

  private dataAccessStream: fs.WriteStream;
  private dataAccessDeniedStream: fs.WriteStream; 

  private constructor() {
    this.dataAccessStream = fs.createWriteStream(LocalFileLogger.dataAccessFile, {
      flags: 'a' // append
    });

    this.dataAccessDeniedStream = fs.createWriteStream(LocalFileLogger.dataAccessDeniedFile, {
      flags: 'a' // append
    });

   }

  public static getInstance(): ILogger {
    return LocalFileLogger.SINGLETON;
  }

  public logDataAccess(rescuerID: string, patientID: string) {
    this.dataAccessStream.write(this.formatLog(rescuerID, patientID));
  }

  public logDataAccessDenied(rescuerID: string, patientID: string) {
    this.dataAccessDeniedStream.write(this.formatLog(rescuerID, patientID));
  }

  private formatLog(rescuerID: string, patientID: string) {
    return "[" + new Date() + "] rescuer: " + rescuerID + " acceded patient " + patientID + " medical data";
  }

}