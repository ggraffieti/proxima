import * as fs from "fs";
import { AbstractLogger } from "./abstractLogger";
 
export class LocalFileLogger extends AbstractLogger {

  private dataAccessFile;
  private dataAccessDeniedFile;

  private dataAccessStream: fs.WriteStream;
  private dataAccessDeniedStream: fs.WriteStream; 

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