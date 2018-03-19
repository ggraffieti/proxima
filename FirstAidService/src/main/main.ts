import * as express from "express";
import * as bodyParser from "body-parser";
import { DataRequestHandler } from "./handlers/dataRequestHandler"
/*import { DatabaseAccess } from "./databaseAccess"
import { WeeklyWorkShift } from "./workShiftsVerifier/utils/weeklyWorkShift";
import { WorkShift } from "./workShiftsVerifier/utils/workShift";
import * as crypto from "crypto";
import * as fs from "fs";
import {AuthorizationChecker} from "./digitalSignature/authorizationChecker";*/
import { RemoteLogger } from "./logger/remoteLogger"

let app = express();
app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());

app.post("/proxima/medical/firstAid/data", DataRequestHandler.handleDataRequest);

app.listen(9876, () => {
  console.log("HTTP first aid service - listen on port 9876");

  //var publicKey = fs.readFileSync("resources/rsa_1024_pub.pem", "utf8");
  //var privateKey = fs.readFileSync("resources/rsa_1024_priv.pem", "utf8");
  
  //console.log(publicKey);

  /*var signer = crypto.createSign('sha256');
  signer.update("987654321");
  var sign = signer.sign(privateKey, 'base64');

  AuthorizationChecker.verifyDigitalSignature("123456789", "987654321", sign).then((val) => {
    console.log(val + " LOOL");
  }).catch((_) => console.log("ERROR"));*/

});