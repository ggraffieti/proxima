import * as express from "express";
import * as request from "request-promise";
import {ServiceRequestUtils} from "./serviceRequestUtils"
import {OK, FORBIDDEN, BAD_REQUEST} from "./resStatusCodes"

export class RequestHandler {
    private static serviceRequest = ServiceRequestUtils.getInstance();

    public static handleRequest(req: express.Request, res: express.Response) {
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
        request(this.serviceRequest.getUserAuthRequest(targetID, service))
            .then(res => {
                if (res.statusCode == OK) {
                    return request(this.serviceRequest.getDNSRequest(service));
                } else {
                    return Promise.reject(FORBIDDEN);
                }
            }).then(res => {
            if (res.statusCode == OK) {
                return request(this.serviceRequest.getDataRequest(JSON.parse(res.body), targetID, operatorID, signature));
            } else {
                return Promise.reject(FORBIDDEN);
            }
        }).then(res => {
            if (res.statusCode == OK) {
                return mainRes.send(res.body);
            } else {
                return Promise.reject(FORBIDDEN);
            }
        }).catch(err => {
            console.log(err);
            mainRes.status(FORBIDDEN).send("Unauthorized");
        })
    }
}