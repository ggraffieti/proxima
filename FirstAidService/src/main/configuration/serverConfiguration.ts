/**
 * The interface that represents the configuration of the server. It contains addresses of other
 * services and the local physical address of the machine.
 */
export interface IServerConfiguration {
  /**
   * The local IP of the machine where this server is deployed.
   */
  localIp: string;

  /**
   * The port number where this server can be contacted.
   */
  localPort: number;

  /**
   * The IP of the DNS service.
   */
  dnsIp: string;

  /**
   * The port number of the DNS service.
   */
  dnsPort: number;

  /**
   * The IP address of the remote logger.
   */
  remoteLoggerIp: string;

  /**
   * The port number of the remote logger.
   */
  remoteLoggerPort: number;
}