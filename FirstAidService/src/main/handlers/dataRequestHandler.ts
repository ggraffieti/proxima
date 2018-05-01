import {Request, Response} from "express";
import {RequestHandler} from "./abstractRequestHandler";
import { AuthorizationChecker } from "../digitalSignature/authorizationChecker";
import { WorkShiftQueries } from "../workShiftsVerifier/workShiftsQueries";
import { MedicalDataQueries } from "../medicalData/medicalDataQueries";
import { ILogger } from "../logger/ILogger";
import { LoggerFactory } from "../logger/loggerFactory";
import { RescuerShiftError } from "../errors/rescuerShiftError";
import { DatabaseError } from "../errors/databaseError";
import { WrongDigitalSignatureError } from "../errors/wrongDigitalSignatureError";

export class DataRequestHandler extends RequestHandler {

  private static logger: ILogger = LoggerFactory.remoteLogger();

  private constructor() {
    super();
  }

  /**
   * Handle the request of medical data, by a operator.
   * This method authorizes the operator by the checking of digital signature, checks if the 
   * operator work shift if coherent, and retrieve patient data. If the operator is not authorized 
   * returns an unauthorized HTTP error (401). The data access request is always logged in a secure
   * manner, in order to mantain an official document that contains all the requests made to the service. 
   * @param {express.Request} req the request object wrapping all the data sent by the client.
   * @param {express.Response} res the response object used for send a response to the client
   */
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
          return WorkShiftQueries.rescuerAuthorization(operatorID);
      })
      .then((auth) => {
        if (auth) {
          return MedicalDataQueries.getPatientData(targetID)
        }
        else {
          return Promise.reject(new RescuerShiftError());
        }
      })
      .then((data) => {
        res.send(data);
        DataRequestHandler.logger.logDataAccess(operatorID, targetID);
      })
      .catch(error => {
        if (error instanceof DatabaseError) {
          DataRequestHandler.sendServerError(res);
        } 
        else if ((error instanceof WrongDigitalSignatureError) || (error instanceof RescuerShiftError)) {
          DataRequestHandler.sendUnauthorizedError(res);
        }
        else {
          DataRequestHandler.sendServerError(res);
        }
        DataRequestHandler.logger.logDataAccessDenied(operatorID, targetID);
      });
    }
    else {
      DataRequestHandler.sendBadRequestError(res);
    }
  }

}