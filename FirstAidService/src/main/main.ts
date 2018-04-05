import * as express from "express";
import * as bodyParser from "body-parser";
import { DataRequestHandler } from "./handlers/dataRequestHandler";
import { KeyManagerHandler } from "./handlers/keyManagerHandler";
import { DatabaseAccess } from "./databaseAccess";
import { WeeklyWorkShift } from "./workShiftsVerifier/utils/weeklyWorkShift";
import { WorkShift } from "./workShiftsVerifier/utils/workShift";
import * as crypto from "crypto";
import * as fs from "fs";
import { FileServerConfiguration } from "./configuration/fileServerConfiguration";
import {AuthorizationChecker} from "./digitalSignature/authorizationChecker";
import { PublicKeyQueries } from "./digitalSignature/publicKeyQueries";
import { DnsRegistration } from "./network/dnsRegistration";

 
let dataApp = express();
let keyManagerApp = express();

dataApp.use(bodyParser.urlencoded({extended: false}));
dataApp.use(bodyParser.json());

DnsRegistration.register();

keyManagerApp.use(bodyParser.urlencoded({extended: false}));
keyManagerApp.use(bodyParser.json());

dataApp.get("/data", DataRequestHandler.handleDataRequest);

keyManagerApp.post("/addKey", KeyManagerHandler.handleKeyInsertion);
keyManagerApp.put("/substituteKey/:rescuerID", KeyManagerHandler.handleKeySubstitution);
keyManagerApp.delete("/deleteKey/:rescuerID", KeyManagerHandler.handleKeyDeletion);


keyManagerApp.listen(9877, () => {
  console.log("HTTP Key Manager - listen on port 9877");
});

dataApp.listen(9876, () => {
  console.log("HTTP First Aid Service - listen on port 9876");
  //  var x = FileServerConfiguration.getInstance();

  //  console.log(x.localIp + ":" + x.localPort + " - " + x.dnsIp + ":" + x.dnsPort + " - " + x.remoteLoggerIp + ":" + x.remoteLoggerPort);

  // sorry for this comment section, but is useful for now, because contains some useful testing code

  /*var publicKey = fs.readFileSync("resources/rsa_1024_pub.pem", "utf8");
  var privateKey = fs.readFileSync("resources/rsa_1024_priv.pem", "utf8");
  
  //console.log(publicKey);

  var signer = crypto.createSign('RSA-SHA256');
  var data = new Buffer(512);
  data = fs.readFileSync("resources/lol.txt");
  signer.update("patient1rescuer1");
  var sign = signer.sign(privateKey, 'base64');
  console.log(sign);

  console.log("\n");*/

 /* var hash = crypto.createHash('RSA-SHA256');
  hash.update(data);
  var dig = hash.digest();
  var cr7 = crypto.privateEncrypt(privateKey, dig).toString("base64");
  console.log(cr7);

  var verifier = crypto.createVerify('sha256');
  verifier.update("1234");
  console.log(verifier.verify(publicKey, cr7, "base64"));*/

  /*AuthorizationChecker.verifyDigitalSignature("123456789", "987654321", sign).then((val) => {
    console.log(val + " LOOL");
  }).catch((_) => console.log("ERROR"));*/


});