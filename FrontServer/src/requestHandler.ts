import * as express from "express";
import * as request from "request-promise";
import {RequestsCreator} from "./requestsUtils"
import {BAD_REQUEST, OK, UNAUTHORIZED} from "http-status-codes";

/**
 * This class provides a set of static methods that could be used to entirely manage a web request, computing the
 * appropriate response.
 */
export class RequestHandler {

    /**
     * This method takes care of analyzing a data request, computing an appropriate response and sending it to the
     * original sender.
     * @param {e.Request} req the data request, must be an HTTP(S) GET request, containing 'targetID', 'operatorID',
     * 'service' and 'signature' as query parameters.
     * @param {e.Response} res the data response, if data retrieving is successful (status code 200), it will contain the
     * requested data of the target subject (in JSON format); otherwise, an unauthorized issue will be thrown (status code 401).
     */
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

    private static computeResponse(targetID: string, operatorID: string, service: string, signature: string, mainRes: express.Response) {
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