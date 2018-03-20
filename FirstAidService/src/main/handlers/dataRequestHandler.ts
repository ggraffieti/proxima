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
    DataRequestHandler.prepareResponse(res);
    let operatorID = req.body.operatorID;
    let targetID = req.body.targetID;
    let signature = req.body.signature;

    if (operatorID && targetID && signature) {
      console.log("handleDataRequest");
      console.log("operator: " + req.body.operatorID);

      AuthorizationChecker.verifyDigitalSignature(req.body.operatorID, req.body.targetID, req.body.signature)
      .then((_) => {
          console.log("verify digital signature OK");
          return WorkShiftQueries.rescuerAuthorization(req.body.operatorID);
      })
      .then((auth) => {
        if (auth) {
          console.log("Work Shift OK");
          return MedicalDataQueries.getPatientData(req.body.targetID)
        }
        else {
          console.log("Work Shift NOOOOOO");
          return Promise.reject("false work schedule");
        }
      })
      .then((data) => res.send(data))
      .catch((err) => {
        console.log(err);
        DataRequestHandler.sendUnauthorizedError(res);
      });
    }
    else {
      res.status(400).send(); // BAD REQUEST.
    }
  }

}