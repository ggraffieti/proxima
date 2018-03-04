identifiers(RESCUER_ID, PATIENT_ID) :- identifier(RESCUER_ID), patientid(PATIENT_ID), retract(patientid(PATIENT_ID)).

findNearest(ID) :- nearest(ID), retract(nearest(ID)).