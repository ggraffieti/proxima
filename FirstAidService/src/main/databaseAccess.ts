import * as mongoose from "mongoose";

/**
 * A class that manage the access to database(s).
 * This class can be instantiate only once, so the pattern singleton is used, in order to 
 * fulfill this property.
 */
export class DatabaseAccess {

  private static SINGLETON = new DatabaseAccess();

  private medicalDataUrl = 'mongodb://localhost:27017/proximaFirstAid';
  private medicalDataConnection;
  private keyDataUrl = 'mongodb://localhost:27017/proximaRescuersKeys';
  private keyDataConnection;

  /**
   * gets the instance of the singleton class.
   */
  public static getInstance() {
    return DatabaseAccess.SINGLETON;
  }

  private constructor() {
    // connect to the medical data database.
    this.medicalDataConnection = mongoose.createConnection(this.medicalDataUrl);

    // show an error if something went wrong.
    this.medicalDataConnection.on('error', console.error.bind(console, 'connection error:'));

    //connect to the public key database
    this.keyDataConnection = mongoose.createConnection(this.keyDataUrl);

    // show an error if something went wrong
    this.keyDataConnection.on('error', console.error.bind(console, 'connection error: '));
  }

  /**
   * Returns the connection to the medical data collection, where are stored medical data and rescuers
   * work shifts.
   */
  public getMedicalDataConnection(): mongoose.Connection {
    return this.medicalDataConnection;
  }

  /**
   * Returns the connection to the public keys collection, whitch contains public keys of rescuers.
   */
  public getKeyDataConnection(): mongoose.Connection {
    return this.keyDataConnection;
  }

}
