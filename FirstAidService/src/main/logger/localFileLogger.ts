import * as fs from "fs";
import { AbstractLogger } from "./abstractLogger";
 
/**
 * A logger that log in a local file.
 */
export class LocalFileLogger extends AbstractLogger {

  private dataAccessFile;
  private dataAccessDeniedFile;

  private dataAccessStream: fs.WriteStream;
  private dataAccessDeniedStream: fs.WriteStream; 

  /**
   * Build a new LocalFileLogger, that log data in the given files.
   * @param dataAccessFile Path to the file where authorized data access will be logged. If the file 
   * does not exists, it will be created automatically.
   * @param dataAccessDeniedFile Path to the file where unauthorized data access will be logged.
   * If the file does not exists, it will be created automatically.
   */
  public constructor(dataAccessFile: string, dataAccessDeniedFile: string) {
    super();
    this.dataAccessFile = dataAccessFile;
    this.dataAccessDeniedFile = dataAccessDeniedFile;

    this.dataAccessStream = fs.createWriteStream(this.dataAccessFile, {
      flags: 'a' // append
    });

    this.dataAccessDeniedStream = fs.createWriteStream(this.dataAccessDeniedFile, {
      flags: 'a' // append
    });

   }

  public logDataAccess(rescuerID: string, patientID: string) {
    this.dataAccessStream.write(this.formatLog(rescuerID, patientID));
  }

  public logDataAccessDenied(rescuerID: string, patientID: string) {
    this.dataAccessDeniedStream.write(this.formatLog(rescuerID, patientID));
  }

}