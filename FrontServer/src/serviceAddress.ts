const REQUEST_TIMEOUT = 1000;

const USERS_AUTH_IP = "localhost";
const USERS_AUTH_PORT = 8436;
const USERS_AUTH_PATH = "authorize";
export function getUserAuthRequest(targetID: String, service: String) {
    return {
        method: 'GET',
        uri: "http://" + USERS_AUTH_IP + ":" + USERS_AUTH_PORT + "/" + USERS_AUTH_PATH + "?targetID=" + targetID + "&service=" + service,
        timeout: REQUEST_TIMEOUT,
        resolveWithFullResponse: true
    };
}

const DNS_IP = "localhost";
const DNS_PORT = 6041;
const DNS_PATH = "dns/address";
export function getDNSRequest(service: String) {
    return {
        method: 'GET',
        uri: "http://" + DNS_IP + ":" + DNS_PORT + "/" + DNS_PATH + "/" + service,
        timeout: REQUEST_TIMEOUT,
        resolveWithFullResponse: true
    };
}

const DATA_PATH = "data";
export function getDataRequest(address: { ip: string, port: number }, targetID: String, operatorID: String) {
    return {
        method: 'GET',
        uri: "http://" + address.ip + ":" + address.port + "/" + DATA_PATH + "?targetID=" + targetID + "&operatorID=" + operatorID,
        timeout: REQUEST_TIMEOUT,
        resolveWithFullResponse: true
    };
}