import * as express from "express";
import {DatabaseConnection} from "./databaseConnection";
import {RequestHandler} from "./requestHandler";

var app = express();
const mongoose = require('mongoose');

DatabaseConnection.connect();

app.get('/authorize', RequestHandler.handleRequest);

app.listen(8436, function () {
    console.log('Authorizer service listening on port 8436.');
});


//http://localhost:8436/authorize?targetid=5aa6708e5f74cbfe1566b984&serviceid=proxima.medical.firstAid
