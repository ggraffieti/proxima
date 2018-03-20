export interface IMedicalData { // readOnly?
  name: string;
  surname: string;
  CF: string;
  birthDate: Date;
  bloodGroup: string;
  organDonor: string;
  medicalConditions: string[];
  drugAllergies: string[];
  otherAllergies: string[];
  medications: string[];
}