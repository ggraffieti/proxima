const USERS_AUTH_IP = "localhost";
const USERS_AUTH_PORT = 8436;
const USERS_AUTH_PATH = "authorize";
export function getUsersAuthURL(targetID: String, service: String) {
    return "http://" + USERS_AUTH_IP + ":" + USERS_AUTH_PORT + "/" + USERS_AUTH_PATH + "?targetID=" + targetID + "service=" + service;
}

const DNS_IP = "localhost";
const DNS_PORT = 6041;
const DNS_PATH = "dns/address";
export function getDnsURL(service: String) {
    return "http://" + DNS_IP + ":" + DNS_PORT + "/" + DNS_PATH + "/" + service;
}

const DATA_PATH = "data";
export function getDataURL(address: Address, targetID: String, operatorID: String) {
    return "http://" + address.ip + ":" + address.port + "/" + DATA_PATH + "?targetID=" + targetID + "&operatorID=" + operatorID;
}

export interface Address {
    ip: string;
    port: number;
}