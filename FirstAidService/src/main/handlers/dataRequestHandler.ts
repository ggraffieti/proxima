import {Request, Response} from "express";
import {RequestHandler} from "./abstractRequestHandler";
import { AuthorizationChecker } from "../digitalSignature/authorizationChecker";
import { WorkShiftQueries } from "../workShiftsVerifier/workShiftsQueries";
import { MedicalDataQueries } from "../medicalData/medicalDataQueries";
import { ILogger } from "../logger/ILogger";
import { LoggerFactory } from "../logger/loggerFactory";

export class DataRequestHandler extends RequestHandler {

  private static logger: ILogger = LoggerFactory.remoteLogger();

  private constructor() {
    super();
  }

  public static handleDataRequest(req: Request, res: Response) {
    DataRequestHandler.prepareResponse(res);

    let operatorID: string = req.query.operatorID;
    let targetID: string = req.query.targetID;
    let signature: string = req.query.signature;

    if (operatorID && targetID && signature) {
      console.log("handleDataRequest");
      console.log("operator: " + operatorID);

      // In URL the character '+' is an alias for whitespace, so node, automatically replaces + with whitespaces. We need to undo this replacement manually.
      signature = signature.split(" ").join("+"); 

      AuthorizationChecker.verifyDigitalSignature(operatorID, targetID, signature)
      .then((_) => {
          console.log("verify digital signature OK");
          return WorkShiftQueries.rescuerAuthorization(operatorID);
      })
      .then((auth) => {
        if (auth) {
          console.log("Work Shift OK");
          return MedicalDataQueries.getPatientData(targetID)
        }
        else {
          console.log("Work Shift NOOOOOO");
          return Promise.reject("false work schedule");
        }
      })
      .then((data) => {
        res.send(data);
        DataRequestHandler.logger.logDataAccess(operatorID, targetID);
      })
      .catch((err) => {
        console.log(err);
        DataRequestHandler.sendUnauthorizedError(res);
        DataRequestHandler.logger.logDataAccessDenied(operatorID, targetID);
      });
    }
    else {
      DataRequestHandler.sendBadRequestError(res);
    }
  }

}