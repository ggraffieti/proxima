import {Request, Response} from "express";
import {RequestHandler} from "./abstractRequestHandler";
import { AuthorizationChecker } from "../digitalSignature/authorizationChecker";
import { WorkShiftQueries } from "../workShiftsVerifier/workShiftsQueries";

export class DataRequestHandler extends RequestHandler {

  private constructor() {
    super();
  }

  public static handleDataRequest(req: Request, res: Response) {
    DataRequestHandler.prepareResponse(res);
    AuthorizationChecker.verifyDigitalSignature(req.body.operatorID, req.body.targetID, req.body.signature)
    .then((authorized) => {
      if (authorized) {
        WorkShiftQueries.rescuerAuthorization(req.body.operatorID)
        .then((auth) => {
          if (auth) {
            res.send(JSON.stringify({ciao:"bella"}));
          }
          else {
            throw new Error();
          }
        });
      }
      else {
        throw new Error();
      }
    })
    .catch((err) => console.log(err)); // send a 403 ERROR!!
  }

}