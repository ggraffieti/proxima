/**
 * Interface that represents a general logger for the system. Every data request have to be logged,
 * whether it was authorized or not. 
 */
export interface ILogger {
  /**
   * Log an authorized data access. The rescuer was authorized to accede patient data.
   * @param rescuerID the id of the rescuer.
   * @param patientID the id of the patient.
   */
    logDataAccess(rescuerID: string, patientID: string);

    /**
     * Log an unauthorized data access. The rescuer sent a request for patient data, but were not 
     * authorized to accede them. Possibly the rescuer was a malicious.
     * @param rescuerID the id of the rescuer.
     * @param patientID the id of the patient.
     */
    logDataAccessDenied(rescuerID: string, patientID: string); 
}