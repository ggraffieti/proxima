import * as mongoose from "mongoose";

export class DatabaseAccess {

  private static SINGLETON = new DatabaseAccess();

  private medicalDataUrl = 'mongodb://localhost:27017/medicalData';
  private medicalDataConnection;
  //private authorizationDataUrl = 'mongodb://localhost:27017/authorizationData';
  //private authorizationDataConnection;

  public static getInstance() {
    return DatabaseAccess.SINGLETON;
  }

  private constructor() {
    // connect to the medical data database.
    this.medicalDataConnection = mongoose.createConnection(this.medicalDataUrl);

    // show an error if something went wrong.
    this.medicalDataConnection.on('error', console.error.bind(console, 'connection error:'));

    //connect to the authorization database
    //this.authorizationDataConnection = mongoose.createConnection(this.authorizationDataUrl);

    // show an error if something went wrong
    //this.authorizationDataConnection.on('error', console.error.bind(console, 'connection error: '));
  }

  public getMedicalDataConnection(): mongoose.Connection {
    return this.medicalDataConnection;
  }

  // public getAuthorizationDataConnection(): mongoose.Connection {
  //   return this.authorizationDataConnection;
  // }

}
