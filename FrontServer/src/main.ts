import * as express from "express";
import {RequestHandler} from "./requestHandler"

let app = express();

app.get('/data', RequestHandler.handleDataRequest);

app.listen(6041, function () {
    console.log('Proxima front server listening on port 6041.');
});