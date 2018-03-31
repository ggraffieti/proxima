import * as fs from "fs"
import * as ip from "ip";
import { IServerConfiguration } from "./serverConfiguration"

/**
 * A class that read the server configuration from a file. It implements the design pattern
 * singleton, so only one instance of this class is active at any moment. 
 * The method load have to be called before any use of this class.
 */
export class FileServerConfiguration implements IServerConfiguration {

  private static filePath = "resources/serverConfiguration.prox";
  private static SINGLETON: IServerConfiguration;

  private constructor(public localIp: string, public localPort: number, public dnsIp: string, public dnsPort: number, public remoteLoggerIp: string, public remoteLoggerPort: number) {}

  /**
   * Returns the only instance of this class. 
   */
  public static getInstance(): IServerConfiguration {
    return this.SINGLETON;
  }

  /**
   * Loads the configuration of the server from a file, and create a suitable instance of this class,
   * containing all the configuration data. 
   * 
   * NOTE: this method HAVE TO be called BEFORE any use of this class.
   */
  public static load() {
    let tmpIpMap: {[serviceName: string]: string} = {};
    let tmpPortMap: {[serviceName: string]: number} = {};
    let configFile = fs.readFileSync(FileServerConfiguration.filePath, "utf8");
    configFile.split("\n").filter((s) => s.length > 0 && s != undefined).forEach((s) => {
      var splitted = s.split("-");
      tmpIpMap[splitted[0]] = splitted[1].split(":")[0];
      tmpPortMap[splitted[0]] = +splitted[1].split(":")[1];
    });
    
    FileServerConfiguration.SINGLETON = new FileServerConfiguration(ip.address(), 9876, tmpIpMap["dns"], tmpPortMap["dns"], tmpIpMap["logger"], tmpPortMap["logger"]);
  }

}