import { expect } from 'chai';
import 'mocha';
import * as crypto from "crypto";
import * as fs from "fs";
import { AuthorizationChecker } from "../../src/digitalSignature/authorizationChecker";

describe('Authorization checker', () => {

  it('should correctly verify a digital signature', () => {
    const data = "abcdefg";
    const signer = crypto.createSign("sha256");
    signer.update(data);
    const privateKey = fs.readFileSync("test/resources/correct_private.pem", "utf8");
    const digitalSignature = signer.sign(privateKey, "base64");
    return AuthorizationChecker.verifyDigitalSignature("123456789", data, digitalSignature)
      .then((res)=> expect(res).to.equal(true));
  });

  it('should reject authentication if rescuer is not present', () => {
    const data = "qwerty";
    const signer = crypto.createSign("sha256");
    signer.update(data);
    const privateKey = fs.readFileSync("test/resources/correct_private.pem", "utf8");
    const digitalSignature = signer.sign(privateKey, "base64");
    return AuthorizationChecker.verifyDigitalSignature("aFakeRescuer", data, digitalSignature)
      .then((res)=> {throw new Error('Expected method to reject.')})
      .catch((_) => expect(_));
  });

  it('should reject authentication if digital signature is wrong', () => {
    const data = "qwerty";
    const signer = crypto.createSign("sha256");
    signer.update(data);
    const privateKey = fs.readFileSync("test/resources/fake_private.pem", "utf8");
    const digitalSignature = signer.sign(privateKey, "base64");
    return AuthorizationChecker.verifyDigitalSignature("123456789", data, digitalSignature)
      .then((res)=> {throw new Error('Expected method to reject.')})
      .catch((_) => expect(_));

  });

});