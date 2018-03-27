import * as fs from "fs"
import * as ip from "ip";
import { IServerConfiguration } from "./serverConfiguration"

export class FileServerConfiguration implements IServerConfiguration {

  private static filePath = "resources/serverConfiguration.prox";
  private static SINGLETON: IServerConfiguration;

  private constructor(public localIp: string, public localPort: number, public dnsIp: string, public dnsPort: number, public remoteLoggerIp: string, public remoteLoggerPort: number) {}

  public static getInstance() {
    return this.SINGLETON;
  }

  public static load() {
    let tmpIpMap: {[serviceName: string]: string} = {};
    let tmpPortMap: {[serviceName: string]: number} = {};
    let configFile = fs.readFileSync(FileServerConfiguration.filePath, "utf8");
    configFile.split("\n").forEach((s) => {
      var splitted = s.split("-");
      tmpIpMap[splitted[0]] = splitted[1].split(":")[0];
      tmpPortMap[splitted[0]] = +splitted[1].split(":")[1];
    });
    
    FileServerConfiguration.SINGLETON = new FileServerConfiguration(ip.address(), 9876, tmpIpMap["dns"], tmpPortMap["dns"], tmpIpMap["logger"], tmpPortMap["logger"]);

  }

}