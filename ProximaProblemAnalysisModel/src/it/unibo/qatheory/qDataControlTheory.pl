authorized(SENDER, RESCUER_ID, PATIENT_ID) :- request(SENDER, RESCUER_ID, PATIENT_ID), rescuer(RESCUER_ID).
		
response(SENDER, PATIENT_ID, NAME, SURNAME, BLOOD_GROUP) :- senderRequestData(SENDER, RESCUER_ID, PATIENT_ID),
	data(PATIENT_ID, NAME, SURNAME, BLOOD_GROUP).