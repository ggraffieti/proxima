export class WrongDigitalSignatureError extends Error {

  constructor(errorMessage?: string) {
    super(errorMessage);
  }

}