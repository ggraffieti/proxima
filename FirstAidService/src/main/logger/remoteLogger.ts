import { AbstractLogger } from "./abstractLogger";
import { NetworkManager } from "../network/networkManager";
import { FileServerConfiguration } from "../configuration/fileServerConfiguration";

/**
 * Represents a remote logger, that log in a remote machine, via network. 
 * If a log is not correctly sent (network error, logger down...) this class
 * automatically resend the log message every second, until it is correctly received.
 */
export class RemoteLogger extends AbstractLogger {

  private loggerAddress: string;
  private static logAccessUrl: string = "logAccess";
  private static locDeniedUrl: string = "logDeny";

  public constructor(address: string) {
    super();
    this.loggerAddress = address;
  }

  public logDataAccess(rescuerID: string, patientID: string) {
    NetworkManager.sendHttpPost(this.loggerAddress + RemoteLogger.logAccessUrl, {
      rescuerID: rescuerID,
      patientID: patientID
    }, (error, response, _) => {
      if (error || response.statusCode != 200) {
        this.tryToRelog(() => this.logDataAccess(rescuerID, patientID));
      }
    });
  }

  public logDataAccessDenied(rescuerID: string, patientID: string) {
    NetworkManager.sendHttpPost(this.loggerAddress + RemoteLogger.locDeniedUrl, {
      rescuerID: rescuerID,
      patientID: patientID
    }, (error, response, _) => {
      if (error || response.statusCode != 200) {
        this.tryToRelog(() => this.logDataAccessDenied(rescuerID, patientID));
      }
    });
  }

  private tryToRelog(loggingFunction: (rescuerID: string, patientID: string) => void) {
    setTimeout(loggingFunction, 1000);
  }

}