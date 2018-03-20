import * as fs from "fs";
import { resolve } from "path";

export class Logger {

  private static dataAccessFile: string = "log/dataAccess.txt";
  private static dataAccessDeniedFile: string = "log/dataAccessDenied.txt";
  private static SINGLETON = new Logger();

  private dataAccessStream: fs.WriteStream;
  private dataAccessDeniedStream: fs.WriteStream; 

  private constructor() {
    this.dataAccessStream = fs.createWriteStream(Logger.dataAccessFile, {
      flags: 'a' // append
    });

    this.dataAccessDeniedStream = fs.createWriteStream(Logger.dataAccessDeniedFile, {
      flags: 'a' // append
    });

   }

  public static getInstance(): Logger {
    return Logger.SINGLETON;
  }


  public logDataAccess(rescuerID: string, patientID: string): Promise<boolean> {
    return new Promise((resolve, reject) => {
      if(this.dataAccessStream.write(this.formatLog(rescuerID, patientID))) {
        resolve(true);
      }
      else {
        reject();
      }
    });
  }

  public logDataAccessDenied(rescuerID: string, patientID: string): Promise<boolean> {
    return new Promise((resolve, reject) => {
      if(this.dataAccessDeniedStream.write(this.formatLog(rescuerID, patientID))) {
        resolve(true);
      }
      else {
        reject();
      }
    });
  }

  protected formatLog(rescuerID: string, patientID: string) {
    return "[" + new Date() + "] rescuer: " + rescuerID + " acceded patient " + patientID + " medical data" + "\n";
  } 

}