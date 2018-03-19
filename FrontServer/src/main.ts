import * as express from "express";
import {RequestHandler} from "./requestHandler"

let app = express();

app.get('/data', RequestHandler.handleRequest);

app.listen(1111, function () {
    console.log('Proxima front server listening on port 1111.');
});