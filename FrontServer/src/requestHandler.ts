import * as express from "express";
import * as request from "request-promise-native";
import {getDnsURL, getUsersAuthURL, getDataURL, Address} from "./serviceAddress"
import {OK, FORBIDDEN, BAD_REQUEST} from "./resStatusCodes"

export class RequestHandler {

    public static handleRequest(req: express.Request, res: express.Response) {
        let targetID = req.query.targetID;
        let operatorID = req.query.operatorID;
        let service = req.query.service;

        if (targetID != undefined && operatorID != undefined && service != undefined) {
            RequestHandler.computeResponse(targetID, operatorID, service, res);
        } else {
            res.sendStatus(BAD_REQUEST);
        }
    }

    private static computeResponse(targetID: String, operatorID: String, service: String, res: express.Response) {
        request.get(getUsersAuthURL(targetID, service))
            .then(body => {
                if (body.statusCode == OK) {
                    return request.get(getDnsURL(service));
                } else {
                    return Promise.reject(FORBIDDEN);
                }
            }).then(body => {
            if (body.statusCode == OK) {
                let address: Address = JSON.parse(body);
                return request.get(getDataURL(address, targetID, operatorID));
            } else {
                return Promise.reject(FORBIDDEN);
            }
        }).then(body => {
            if (body.statusCode(OK)) {
                return res.send(body);
            } else {
                return Promise.reject(FORBIDDEN);
            }
        }).catch(err => {
            console.log(err);
            res.sendStatus(FORBIDDEN);
        })
    }
}