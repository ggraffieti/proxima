import { ILogger } from "./ILogger";
import { RemoteLogger } from "./remoteLogger";
import { LocalFileLogger } from "./localFileLogger";

export class LoggerFactory {

  public static localFileLogger(logAccesFilePath: string, logDenyFilePath: string): ILogger {
    return new LocalFileLogger(logAccesFilePath, logDenyFilePath); 
  }

  public static remoteLogger(): ILogger {
    return new RemoteLogger();
  }

}