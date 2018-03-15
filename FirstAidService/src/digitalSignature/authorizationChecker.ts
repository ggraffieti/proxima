export class AuthorizationChecker {
  public static verifyDigitalSignature(operatorID: string, targetID: string, digitalSignature:string ): Promise<boolean> {
    return Promise.resolve(true); // mock object, always return true for now.
  }  
}