import {Request, Response} from "express";
import {RequestHandler} from "./abstractRequestHandler";
import { AuthorizationChecker } from "../digitalSignature/authorizationChecker";
import { WorkShiftQueries } from "../workShiftsVerifier/workShiftsQueries";

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
            res.send(JSON.stringify({ciao:"bella"})); // send medical data.
          }
          else {
            throw "false work schedule";
          }
        }).catch((err) => console.log(err));
      }
      else {
        throw "false signature auth";
      }
    })
    .catch((err) => console.log(err)); // send a 403 ERROR!!
  }

}