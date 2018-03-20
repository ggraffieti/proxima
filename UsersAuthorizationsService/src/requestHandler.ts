import * as express from "express";
import {userAuthorizations} from "./userAuthorizationsModel";

export class RequestHandler {

  private static BAD_REQUEST_ERROR_CODE = 400;
  private static FORBIDDEN_ERROR_CODE = 403;
  private static INTERNAL_ERROR_CODE = 500;

  public static handleRequest(req: express.Request, res: express.Response) {
    var targetId = req.query.targetID;
    var serviceId = req.query.service;

    if(targetId !== undefined && serviceId !== undefined) {
      userAuthorizations.findOne({targetID : targetId, authorizations : serviceId}, (err, doc) => {
        if (err) {
          RequestHandler.sendError(res, RequestHandler.INTERNAL_ERROR_CODE, 'Internal Error');
        } else if(!doc) {
          RequestHandler.sendError(res, RequestHandler.FORBIDDEN_ERROR_CODE, 'Forbidden');
        } else {
          res.send("Authorized");
          console.log(doc.toString());
        }
      });
    } else {
      RequestHandler.sendError(res, RequestHandler.BAD_REQUEST_ERROR_CODE, 'Bad request');
    }
  }

  private static sendError(res: express.Response, code: number, errorMessage: string) {
    res.status(code).send(errorMessage);
  }
}
