import * as express from "express";
import {userAuthorizations} from "./userAuthorizationsModel";

export class RequestHandler {

  private static BAD_REQUEST_ERROR_CODE = 400;
  private static FORBIDDEN_ERROR_CODE = 403;
  private static INTERNAL_ERROR_CODE = 500;

  public static handleRequest(req: express.Request, res: express.Response) {
    var targetId = req.query.targetid;
    var serviceId = req.query.serviceid;

    if(targetId !== undefined && serviceId !== undefined) {
      userAuthorizations.findOne({'targetID' : targetId}, 'authorizations', (err, doc) => {
        if (err || doc == undefined) {
          RequestHandler.sendError(res, RequestHandler.INTERNAL_ERROR_CODE, 'Internal Error');
        } else if(doc.authorizations.includes(serviceId)) {
          res.send("Authorized");
        } else {
          RequestHandler.sendError(res, RequestHandler.FORBIDDEN_ERROR_CODE, 'Forbidden');
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
