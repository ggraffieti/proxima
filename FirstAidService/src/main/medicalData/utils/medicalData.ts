import { IMedicalData } from "./IMedicalData";

/**
 * A simple implementation of the interface IMedicalData.
 */
export class MedicalData implements IMedicalData {
  /**
   * Build a new MedicalData object, containing first aid specific medical data of one patient.
   * @param name the name of the patient.
   * @param surname the surname of the patient.
   * @param CF the fiscal code of the patient (Codice Fiscale).
   * @param birthDate the birth date of the patient.
   * @param bloodGroup  the blood group of the patient (A+,A-,B+,B-,AB+,AB-,0+,0-)
   * @param organDonor YES if the patient is an organ donor, NO otherwise.
   * @param medicalConditions  array of medical conditions of the patient (e.g. pacemaker, protesis..)
   * @param drugAllergies  array of drug allergies of the patient (e.g. morphin, ibuprofen..)
   * @param otherAllergies  array of allergy of the patient (e.g. latex, peanuts...)
   * @param medications  array of drugs that the patient takes regularly (e.g. cardioaspirin...) 
   */
  public constructor(public name: string, public surname: string, public CF: string, public birthDate: Date, public bloodGroup: string, public organDonor: string, public medicalConditions: string[] = [], public drugAllergies: string[] = [], public otherAllergies: string[] = [], public medications: string[] = []) { }
}