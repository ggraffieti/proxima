import { Request, Response } from "express";
import { RequestHandler } from "./abstractRequestHandler";
import { PublicKeyQueries } from "../digitalSignature/publicKeyQueries";

export class KeyManagerHandler extends RequestHandler {

  private constructor() {
    super();
  }

  public static handleKeyInsertion(req: Request, res: Response) {
    KeyManagerHandler.prepareResponse(res);

    const rescuerID = req.body.rescuerID;
    const key = req.body.privateKey;

    if (rescuerID && key) {
      PublicKeyQueries.addPublicKey(rescuerID, key)
        .then((_) => res.send())
        .catch((_) => KeyManagerHandler.sendServerError(res)); // temporary. Need more accurate error messages!
    }
    else {
      KeyManagerHandler.sendBadRequestError(res);
    }
  }

  public static handleKeySubstitution(req: Request, res: Response) {
    KeyManagerHandler.prepareResponse(res); 

    const rescuerID = req.params.rescuerID;
    const key = req.body.privateKey;

    if (rescuerID && key) {
      PublicKeyQueries.substitutePublicKey(rescuerID, key)
        .then((_) => res.send())
        .catch((_) => KeyManagerHandler.sendServerError(res)); // temporary. Need more accurate error messages!
    }
    else {
      KeyManagerHandler.sendBadRequestError(res);
    }
  }

  public static handleKeyDeletion(req: Request, res: Response) {
    KeyManagerHandler.prepareResponse(res);

    const rescuerID = req.body.rescuerID;

    if (rescuerID) {
      PublicKeyQueries.deletePublicKey(rescuerID)
        .then((_) => res.send())
        .catch((_) => KeyManagerHandler.sendServerError(res)); // temporary. Need more accurate error messages!
    }
    else {
      KeyManagerHandler.sendBadRequestError(res);
    }
  }

}