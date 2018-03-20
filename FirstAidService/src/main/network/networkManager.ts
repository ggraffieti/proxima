import * as request from "request";

export class NetworkManager {

  private static NETWORK_TIMEOUT = 1000; // 1 second

  public static sendHttpGet(url: string, callback: (error, response, body) => void) {
    request.get(url, {timeout: NetworkManager.NETWORK_TIMEOUT}, callback);
  }

  public static sendHttpPost(url: string, data: object, callback: (error, response, body) => void) {
    request.post(url, {timeout: NetworkManager.NETWORK_TIMEOUT, body: data, json: true}, callback);
  }

}