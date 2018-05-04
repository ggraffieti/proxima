import * as mongoose from "mongoose";

export class DatabaseConnection {

    private static url = 'mongodb://localhost/proximaUsersAuthorizations';

    public static connect() {
      mongoose.connect(DatabaseConnection.url);
      mongoose.connection.on('error', console.error.bind(console, 'connection error:'));
      return mongoose.connection;
    }
}
