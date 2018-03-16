import {Request, Response} from "express";
import {RequestHandler} from "./abstractRequestHandler";
import { AuthorizationChecker } from "../digitalSignature/authorizationChecker";
import { WorkShiftQueries } from "../workShiftsVerifier/workShiftsQueries";
import { MedicalDataQueries } from "../medicalData/medicalDataQueries";

export class DataRequestHandler extends RequestHandler {

  private constructor() {
    super();
  }

  public static handleDataRequest(req: Request, res: Response) {
    console.log("handleDataRequest");
    console.log("operator: " + req.body.operatorID);
    DataRequestHandler.prepareResponse(res);
    AuthorizationChecker.verifyDigitalSignature(req.body.operatorID, req.body.targetID, req.body.signature)
    .then((authorized) => {
      if (authorized) {
        console.log("verify digital signature OK");
        WorkShiftQueries.rescuerAuthorization(req.body.operatorID).then((auth) => {
          console.log("Work Shift OK");
          if (auth) {
            MedicalDataQueries.getPatientData(req.body.targetID).then((data) => res.send(data))
              .catch((err) => console.log(err));
          }
          else {
            throw "false work schedule";
          }
        }).catch((err) => {
          console.log(err);
          DataRequestHandler.sendUnauthorizedError(res);
        });
      }
      else {
        throw "false signature auth";
      }
    })
    .catch((err) => {
      console.log(err);
      DataRequestHandler.sendUnauthorizedError(res);
    });
  }

}