import * as express from "express";
import * as mongoose from "mongoose";
import {DatabaseConnection} from "./databaseConnection";
import {RequestHandler} from "./requestHandler";

var app = express();

DatabaseConnection.connect();

app.get('/authorize', RequestHandler.handleRequest);

app.listen(8436, function () {
    console.log('Authorization service listening on port 8436.');
});


//http://localhost:8436/authorize?targetid=5aa6708e5f74cbfe1566b984&serviceid=proxima.medical.firstAid
