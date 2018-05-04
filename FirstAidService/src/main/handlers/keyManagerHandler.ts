import { Request, Response } from "express";
import { RequestHandler } from "./abstractRequestHandler";
import { PublicKeyQueries } from "../digitalSignature/publicKeyQueries";

/**
 * An Handler class, that handles public key requests (insertion, deletion, managing).
 *
 * This class is static, it doesn't have state and it cannot be istantiate.
 */
export class KeyManagerHandler extends RequestHandler {

  private constructor() {
    super();
  }

  /**
   * Handle the insertion of a new public key for a new rescuer.
   * @param {express.Request} req the request object wrapping all the data sent by the client.
   * @param {express.Response} res the response object used for send a response to the client
   */
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

  /**
   * Handle the substitution of the public key of a rescuer.
   * @param {express.Request} req the request object wrapping all the data sent by the client.
   * @param {express.Response} res the response object used for send a response to the client
   */
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

  /**
   * Handle the deletion of public key.
   * @param {express.Request} req the request object wrapping all the data sent by the client.
   * @param {express.Response} res the response object used for send a response to the client
   */
  public static handleKeyDeletion(req: Request, res: Response) {
    KeyManagerHandler.prepareResponse(res);

    const rescuerID = req.params.rescuerID;

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