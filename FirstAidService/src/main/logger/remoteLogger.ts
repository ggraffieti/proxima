import { AbstractLogger } from "./abstractLogger";
import { NetworkManager } from "../network/networkManager";

export class RemoteLogger extends AbstractLogger {

  private static loggerAddress: string = "http://localhost:6666/proxima/medical/firstAid/"
  private static logAccessUrl: string = "logAccess";
  private static locDeniedUrl: string = "logDeny";

  public constructor() {
    super();
  }

  public logDataAccess(rescuerID: string, patientID: string) {
    NetworkManager.sendHttpPost(RemoteLogger.loggerAddress + RemoteLogger.logAccessUrl, {
      rescuerID: rescuerID,
      patientID: patientID
    }, (error, response, _) => {
      if (error || response.statusCode != 200) {
        console.log("try to relog");
        this.tryToRelog(() => this.logDataAccess(rescuerID, patientID));
      }
    });
  }

  public logDataAccessDenied(rescuerID: string, patientID: string) {
    NetworkManager.sendHttpPost(RemoteLogger.loggerAddress + RemoteLogger.locDeniedUrl, {
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