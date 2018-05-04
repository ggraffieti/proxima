import { expect } from 'chai';
import 'mocha';
import * as crypto from "crypto";
import * as fs from "fs";
import { AuthorizationChecker } from "../../main/digitalSignature/authorizationChecker";

describe('Authorization checker', () => {

  it('should correctly verify a digital signature', () => {
    const patientID = "abcdefg";
    const operatorID = "rescuer1";
    const signer = crypto.createSign("sha256");
    signer.update(patientID + operatorID);
    const privateKey = fs.readFileSync("src/test/resources/correct_private.pem", "utf8");
    const digitalSignature = signer.sign(privateKey, "base64");
    return AuthorizationChecker.verifyDigitalSignature(operatorID, patientID, digitalSignature)
      .then((res)=> expect(res).to.equal(true));
  });

  it('should reject authentication if rescuer is not present', () => {
    const patientID = "abcdefg";
    const operatorID = "aFakeRescuer";
    const signer = crypto.createSign("sha256");
    signer.update(patientID + operatorID);
    const privateKey = fs.readFileSync("src/test/resources/correct_private.pem", "utf8");
    const digitalSignature = signer.sign(privateKey, "base64");
    return AuthorizationChecker.verifyDigitalSignature(operatorID, patientID, digitalSignature)
      .then((_)=> {throw new Error('Expected method to reject.')})
      .catch((_) => expect(_));
  });

  it('should reject authentication if digital signature is wrong', () => {
    const patientID = "abcdefg";
    const operatorID = "rescuer1";
    const signer = crypto.createSign("sha256");
    signer.update(patientID + operatorID);
    const privateKey = fs.readFileSync("src/test/resources/fake_private.pem", "utf8");
    const digitalSignature = signer.sign(privateKey, "base64");
    return AuthorizationChecker.verifyDigitalSignature(operatorID, patientID, digitalSignature)
      .then((_)=> {throw new Error('Expected method to reject.')})
      .catch((_) => expect(_));
  });

});