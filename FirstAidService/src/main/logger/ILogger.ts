export interface ILogger {
    logDataAccess(rescuerID: string, patientID: string);
    logDataAccessDenied(rescuerID: string, patientID: string); 
}