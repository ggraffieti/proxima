import { IMedicalData } from "./IMedicalData";

/**
 * A simple implementation of the interface IMedicalData.
 */
export class MedicalData implements IMedicalData {
  public constructor(public name: string, public surname: string, public CF: string, public birthDate: Date, public bloodGroup: string, public organDonor: string, public medicalConditions: string[] = [], public drugAllergies: string[] = [], public otherAllergies: string[] = [], public medications: string[] = []) { }
}