import * as express from "express";
import * as bodyParser from "body-parser";
import { HandleRequest } from "./handler/handleRequest"

let app = express();
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

app.post("/proxima/medical/firstAid/logAccess", HandleRequest.handleAccessLogRequest);
app.post("/proxima/medical/firstAid/logDeny", HandleRequest.handleDeniedLogRequest);

app.listen(6666, () => {
  console.log("Logger listening on port 6666");
});