/**
 * Interface that represents all the first aid medical data of a patient.
 */
export interface IMedicalData {
  /** The name of the patient. */
  readonly name: string;
  /** The surname of the patient. */
  readonly surname: string;
  /** The CF (Codice Fiscale) of the patient. */
  readonly CF: string;
  /** The birth date of the patient. */
  readonly birthDate: Date;
  /** The blood group of the patient (A+,A-,B+,B-,AB+,AB-,0+,0-). */
  readonly bloodGroup: string;
  /** YES if the patient is an organ donor, NO otherwise. */
  readonly organDonor: string;
  /** Array of medical condition of the patient (e.g. pacemaker, heart disease, cancer...) */
  readonly medicalConditions: string[];
  /** Array of drugs, to which the patient is allergic */
  readonly drugAllergies: string[];
  /** An array of substances, to which the patient is allergic (e.g. latex, peanuts, gluten...) */
  readonly otherAllergies: string[];
  /** An array of drugs that the patient takes regularly (e.g. cardioaspirin...) */
  readonly medications: string[];
}