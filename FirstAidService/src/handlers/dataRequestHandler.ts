import {Request, Response} from "express";
import {RequestHandler} from "./abstractRequestHandler";

export class DataRequestHandler extends RequestHandler {

  private constructor() {
    super();
  }

  public static handleDataRequest(req: Request, res: Response) {
    DataRequestHandler.prepareResponse(res);
    res.send(JSON.stringify({ciao:"bella"}));
  }

}