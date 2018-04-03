import * as fs from "fs";
import * as rootPath from "app-root-path";

/**
 * Represents the address of a component, composed of IP address and port number.
 */
interface Address {
    ip: string;
    port: number;
}

/**
 * Represents a Proxima web service. Contains all the information needed in order to contact the service.
 * A service is described by its name, its address (IP address and port number) and the path it listens to.
 */
interface Service {
    name: string;
    address: Address;
    path: string;
}

/**
 * Represents all the information needed in order to make a HTTP(S) request to a Proxima web service.
 * Contains the HTTP method, the component URI, a numeric value indicating the timeout of the request and a boolean value
 * (true if the whole response will be analyzed, false if only the response body will matter).
 */
interface RequestInfo {
    method: string;
    uri: string;
    timeout: number;
    resolveWithFullResponse: boolean
}

/**
 * This class takes care of building all the useful requests to other Proxima services.
 * The services information, needed by this class in order to build the requests, are retrieved from a local JSON file,
 * named 'servicesAddresses.json'.
 * This class adopts the Singleton pattern, avoiding multiple instances during the execution.
 */
export class RequestsCreator {
    private static FILE_PATH = rootPath + '/res/servicesAddresses.json';
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
     * @param {string} targetID the identifier of the target user.
     * @param {string} service the indication of the service in question.
     * @returns {RequestInfo} the request to the Proxima users authorization service.
     */
    getUserAuthRequest(targetID: string, service: string): RequestInfo {
        let srv = this.services.find(s => s.name === "usersAuthorizationsService");
        return {
            method: 'GET',
            uri: "http://" + srv.address.ip + ":" + srv.address.port + "/" + srv.path + "?targetID=" + targetID + "&service=" + service,
            timeout: RequestsCreator.REQUEST_TIMEOUT,
            resolveWithFullResponse: true
        };
    }

    /**
     * This method generates a request to the Proxima DNS service, asking for the address of a component offering the
     * specified service.
     * @param {string} service the indication of the service.
     * @returns {RequestInfo} the request to the Proxima DNS service.
     */
    getDNSRequest(service: string): RequestInfo {
        let srv = this.services.filter(s => s.name === "dnsService")[0];
        return {
            method: 'GET',
            uri: "http://" + srv.address.ip + ":" + srv.address.port + "/" + srv.path + "/" + service,
            timeout: RequestsCreator.REQUEST_TIMEOUT,
            resolveWithFullResponse: true
        };
    }

    /**
     * This method generates a request to a Proxima component, requesting the data of the subject, identified by its
     * targetID. The data could be protected, so the operator that wants to access them have to be authorized, through
     * the indication of its operatorID and its personal signature.
     * @param {Address} address the component address, composed of IP address and port number.
     * @param {string} targetID the identifier of the target user.
     * @param {string} operatorID the identifier of the operator.
     * @param {string} signature the signature of the operator.
     * @returns {RequestInfo} the request to the specified component.
     */
    getDataRequest(address: Address, targetID: string, operatorID: string, signature: string): RequestInfo {
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