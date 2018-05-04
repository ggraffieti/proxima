import { Request, Response } from "express";
import { Logger } from "../logger/logger";


export class HandleRequest {

  /**
   Method that handle the request of a logging for correctly accessed data.
   */
  public static handleAccessLogRequest(req: Request, res: Response) {
    Logger.getInstance().logDataAccess(req.body.rescuerID, req.body.patientID)
      .then((_) => res.send())
      .catch((_) => res.status(500).send());
  }

  /**
   Method that handle the request of a logging for denied data access.
   */
  public static handleDeniedLogRequest(req: Request, res: Response) {
    Logger.getInstance().logDataAccessDenied(req.body.rescuerID, req.body.patientID)
      .then((_) => res.send())
      .catch((_) => res.status(500).send());
  }

}
