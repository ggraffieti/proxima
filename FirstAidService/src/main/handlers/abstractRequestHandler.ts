import {Request, Response} from "express";

/**
 * This abstract class acts as parent class for every handler in the system. It exposes some 
 * utilities methods to child classes.
 * Every handler created for processing a request have to extend this class
 */
export abstract class RequestHandler {

  private static SERVER_ERROR_CODE = 500;
  private static UNAUTHORIZED_ERROR_CODE = 403;
  private static BAD_REQUEST_ERROR_CODE = 400;

  /**
   * Send an HTTP internal server error (500), in the given connection and with an optional error message
   * @param {express.Response} res the response object used for send a response to the client
   * @param {string} errorMessage an optional error message.
   */
  protected static sendServerError(res: Response, errorMessage?: string) {
    this.sendError(res, this.SERVER_ERROR_CODE, errorMessage);
  }

  /**
   * Send an HTTP unauthorized error (403), in the given connection and with an optional error message
   * @param {Response} res the response object used for send a response to the client
   * @param {string} errorMessage an optional error message.
   */
  protected static sendUnauthorizedError(res: Response, errorMessage?: string) {
    this.sendError(res, this.UNAUTHORIZED_ERROR_CODE, errorMessage);
  }

  protected static sendBadRequestError(res: Response, errorMessage?: string) {
    this.sendError(res, this.BAD_REQUEST_ERROR_CODE, errorMessage);
  }

  /**
   * Prepare the given response for sending procedure. Attaches HTTP headers, and set the keep-alive
   * flag to false, because the connection have to be closed immediatly after the response arrival.
   * @param {Response} res the response object used for send a response to the client
   */
  protected static prepareResponse(res: Response) {
    res.shouldKeepAlive = false;
    res.setHeader('Content-Type', 'application/json');
    res.setHeader('Access-Control-Allow-Origin', '*');
  }

  private static sendError(res: Response, errorCode: number, errorMessage?: string) {
    res.status(errorCode).send(errorMessage);
  }

}
