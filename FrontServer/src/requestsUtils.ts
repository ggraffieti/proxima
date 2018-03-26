import * as fs from "fs";

/**
 * Represents a Proxima web service. Contains all the information needed in order to contact the service.
 * A service is described by its name, its address (IP address and port number) and the path it listens to.
 */
interface Service {
    name: String;
    ip: String;
    port: Number;
    path: String;
}

/**
 * This class takes care of building all the useful requests to other Proxima services.
 * The services information, needed by this class in order to build the requests, are retrieved from a local JSON file,
 * named 'servicesAddresses.json'.
 * This class adopts the Singleton pattern, avoiding multiple instances during the execution.
 */
export class RequestsCreator {
    private static FILE_PATH = "../servicesAddresses.json";
    private static REQUEST_TIMEOUT = 1000;
    private static _SINGLETON: RequestsCreator = new RequestsCreator();

    private services: Service[];

    private constructor() {
        fs.readFile(RequestsCreator.FILE_PATH, ((err, data) => {
            if (err) {
                throw err;
            }
            this.services = JSON.parse(data.toString()).services;
        }));
    }

    /**
     * Returns the only instance of this class, following the Singleton pattern.
     * @returns {RequestsCreator} the object of type RequestsCreator.
     */
    public static getInstance() {
        return this._SINGLETON;
    }

    /**
     * This method generates a request to the Proxima users authorization service, asking if the subject, identified by a
     * targetID, allowed data access for the given service.
     * @param {String} targetID the identifier of the target user.
     * @param {String} service the indication of the service in question.
     * @returns {{method: string; uri: string; timeout: number; resolveWithFullResponse: boolean}} the request to the
     * Proxima users authorization service.
     */
    getUserAuthRequest(targetID: String, service: String) {
        let srv = this.services.find(s => s.name === "usersAuthorizationsService");
        return {
            method: 'GET',
            uri: "http://" + srv.ip + ":" + srv.port + "/" + srv.path + "?targetID=" + targetID + "&service=" + service,
            timeout: RequestsCreator.REQUEST_TIMEOUT,
            resolveWithFullResponse: true
        };
    }

    /**
     * This method generates a request to the Proxima DNS service, asking for the address of a component offering the
     * specified service.
     * @param {String} service the indication of the service.
     * @returns {{method: string; uri: string; timeout: number; resolveWithFullResponse: boolean}} the request to the
     * Proxima DNS service.
     */
    getDNSRequest(service: String) {
        let srv = this.services.filter(s => s.name === "dnsService")[0];
        return {
            method: 'GET',
            uri: "http://" + srv.ip + ":" + srv.port + "/" + srv.path + "/" + service,
            timeout: RequestsCreator.REQUEST_TIMEOUT,
            resolveWithFullResponse: true
        };
    }

    /**
     * This method generates a request to a Proxima component, requesting the data of the subject, identified by its
     * targetID. The data could be protected, so the operator that wants to access them have to be authorized, through
     * the indication of its operatorID and its personal signature.
     * @param {{ip: String; port: Number}} address the component address, composed of IP address and port number.
     * @param {String} targetID the identifier of the target user.
     * @param {String} operatorID the identifier of the operator.
     * @param {String} signature the signature of the operator.
     * @returns {{method: string; uri: string; timeout: number; resolveWithFullResponse: boolean}} the request to the
     * specified component.
     */
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