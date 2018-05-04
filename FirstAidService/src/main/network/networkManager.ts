import * as request from "request";

/**
 * A static class that manages the network, sending HTTP requests.
 */
export class NetworkManager {

  private static NETWORK_TIMEOUT = 1000; // 1 second

  /**
   * Send an HTTP GET request.
   * @param url the URL of the request
   * @param callback a callback attached to the request.
   */
  public static sendHttpGet(url: string, callback: (error, response, body) => void) {
    request.get(url, {timeout: NetworkManager.NETWORK_TIMEOUT}, callback);
  }

  /**
   * Send an HTTP POST request.
   * @param url the URL of the request.
   * @param data optional data, attached to the request, in JSON form.
   * @param callback a callback attached to the request.
   */
  public static sendHttpPost(url: string, data: object, callback: (error, response, body) => void) {
    request.post(url, {timeout: NetworkManager.NETWORK_TIMEOUT, body: data, json: true}, callback);
  }

}