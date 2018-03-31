import { ILogger } from "./ILogger";
import { RemoteLogger } from "./remoteLogger";
import { LocalFileLogger } from "./localFileLogger";
import { FileServerConfiguration } from "../configuration/fileServerConfiguration";

/**
 * A factory class that builds loggers.
 * 
 * This class is static, it doesn't have state and it cannot be istantiate.
 */
export class LoggerFactory {

  private constructor() {}

  /**
   * Returns a new instance of a LocalFileLogger, that logs on the given files.
   * @param logAccesFilePath Path to the file where authorized data access will be logged.
   * @param logDenyFilePath Path to the file where unauthorized data access will be logged.
   */
  public static localFileLogger(logAccesFilePath: string, logDenyFilePath: string): ILogger {
    return new LocalFileLogger(logAccesFilePath, logDenyFilePath); 
  }

  /**
   * Returns a new instance of a RemoteLogger, that logs on a remote machine.
   */
  public static remoteLogger(): ILogger {
    return new RemoteLogger("http://" + 
    FileServerConfiguration.getInstance().remoteLoggerIp + ":" +
    FileServerConfiguration.getInstance().remoteLoggerPort +
    "/proxima/medical/firstAid/");
  }

}