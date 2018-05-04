import { ILogger } from "./ILogger";
import { RemoteLogger } from "./remoteLogger";
import { LocalFileLogger } from "./localFileLogger";
import { FileServerConfiguration } from "../configuration/fileServerConfiguration";
import { AbstractLogger } from "./abstractLogger";
import { AbstractConsoleLogger } from "./abstractConsoleLogger";
import { ConsoleLogger } from "./consoleLogger";

/**
 * A factory class that builds loggers.
 * 
 * This class is static, it doesn't have state and it cannot be istantiate.
 */
export class LoggerFactory {

  private static _consoleLogger: AbstractConsoleLogger = undefined;

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

  /**
   * Returns a console logger. Note that only a console logger is instantiate, and the same logger is 
   * always returned. 
   * If this method is called twice, the same instance of console logger is returned.
   */
  public static consoleLogger(): AbstractConsoleLogger {
    if (this.consoleLogger == undefined) {
      this._consoleLogger = new ConsoleLogger();
    }
    return this._consoleLogger;
  }

  /**
   * Returns a new instance of a console logger.
   * If this method is called twice, two different instance of console logger are returned.
   */
  public static newConsoleLogger(): AbstractConsoleLogger {
    return new ConsoleLogger();
  }

}