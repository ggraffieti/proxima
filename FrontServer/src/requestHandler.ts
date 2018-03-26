import * as express from "express";
import * as request from "request-promise";
import {RequestsCreator} from "./requestsUtils"
import {OK, UNAUTHORIZED, BAD_REQUEST} from "./resStatusCodes"

export class RequestHandler {
    public static handleDataRequest(req: express.Request, res: express.Response) {
        let targetID = req.query.targetID;
        let operatorID = req.query.operatorID;
        let service = req.query.service;
        let signature = req.query.signature;

        if (targetID != undefined && operatorID != undefined && service != undefined && signature != undefined) {
            RequestHandler.computeResponse(targetID, operatorID, service, signature, res);
        } else {
            res.sendStatus(BAD_REQUEST);
        }
    }

    private static computeResponse(targetID: String, operatorID: String, service: String, signature: String, mainRes: express.Response) {
        let serviceRequest = RequestsCreator.getInstance();
        request(serviceRequest.getUserAuthRequest(targetID, service))
            .then(res => res.statusCode == OK ?
                request(serviceRequest.getDNSRequest(service)) :
                Promise.reject("User authorization denial"))
            .then(res => res.statusCode == OK ?
                request(serviceRequest.getDataRequest(JSON.parse(res.body), targetID, operatorID, signature)) :
                Promise.reject("Cannot access data"))
            .then(res => res.statusCode == OK ?
                mainRes.send(res.body) :
                Promise.reject("Operator unauthorized"))
            .catch(err => this.handleErrorInDataRetrieving(err, mainRes));
    }

    private static handleErrorInDataRetrieving(err: any, res: express.Response) {
        console.log(err);
        res.sendStatus(UNAUTHORIZED);
    }
}