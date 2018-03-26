import * as fs from "fs";

interface Service {
    name: String;
    ip: String;
    port: Number;
    path: String;
}

export class RequestsCreator {
    private static REQUEST_TIMEOUT = 1000;
    private static _SINGLETON: RequestsCreator = new RequestsCreator();

    private services: Service[];

    private constructor() {
        fs.readFile("../servicesAddresses.json", ((err, data) => {
            if (err) {
                throw err;
            }
            this.services = JSON.parse(data.toString()).services;
        }));
    }

    public static getInstance() {
        return this._SINGLETON;
    }

    getUserAuthRequest(targetID: String, service: String) {
        let srv = this.services.find(s => s.name === "usersAuthorizationsService");
        return {
            method: 'GET',
            uri: "http://" + srv.ip + ":" + srv.port + "/" + srv.path + "?targetID=" + targetID + "&service=" + service,
            timeout: RequestsCreator.REQUEST_TIMEOUT,
            resolveWithFullResponse: true
        };
    }

    getDNSRequest(service: String) {
        let srv = this.services.filter(s => s.name === "dnsService")[0];
        return {
            method: 'GET',
            uri: "http://" + srv.ip + ":" + srv.port + "/" + srv.path + "/" + service,
            timeout: RequestsCreator.REQUEST_TIMEOUT,
            resolveWithFullResponse: true
        };
    }

    getDataRequest(address: { ip: String, port: Number}, targetID: String, operatorID: String, signature: String) {
        let srv = this.services.filter(s => s.name === "dataService")[0];
        return {
            method: 'GET',
            uri: "http://" + address.ip + ":" + address.port + "/" + srv.path +
                 "?targetID=" + targetID + "&operatorID=" + operatorID + "&signature=" + signature,
            timeout: RequestsCreator.REQUEST_TIMEOUT,
            resolveWithFullResponse: true
        };
    }
}