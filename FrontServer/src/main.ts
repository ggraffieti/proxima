import * as express from "express";
import {RequestHandler} from "./requestHandler"
import * as https from "https";
import * as fs from "fs";
import * as rootPath from "app-root-path";

let app = express();

app.get('/data', RequestHandler.handleDataRequest);

// create an https server, listening on port 6041
https.createServer({
    key : fs.readFileSync(rootPath + '/res/HttpsKey.key'),
    cert : fs.readFileSync(rootPath + '/res/HttpsCert.crt')
}, app).listen(6041, function() {
    console.log('Proxima HTTPS front server listening on port 6041!');
});