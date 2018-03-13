import * as express from "express";
import { DataRequestHandler } from "./handlers/dataRequestHandler"
import { DatabaseAccess } from "./databaseAccess"

let app = express();

app.get("/proxima/medical/firstAid/data", DataRequestHandler.handleDataRequest);

app.listen(9876, () => {
  console.log("HTTP first aid service - listen on port 9876");
});